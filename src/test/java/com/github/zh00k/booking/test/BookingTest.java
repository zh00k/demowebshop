package com.github.zh00k.booking.test;

import com.github.zh00k.booking.clients.BookingApiClient;
import com.github.zh00k.booking.config.BookingApiConfig;
import com.github.zh00k.booking.config.BookingConfig;
import com.github.zh00k.booking.dto.AuthResponse;
import com.github.zh00k.booking.dto.Booking;
import com.github.zh00k.booking.dto.BookingDates;
import com.github.zh00k.booking.dto.BookingResponse;
import com.github.zh00k.booking.steps.BookingSteps;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

public class BookingTest extends BaseApiTest {
    private static final Faker                faker              = new Faker();
    private static final BookingConfig        cfg                = BookingApiConfig.getBookingConfig();
    private static final RequestSpecification bookingRequestSpec = new RequestSpecBuilder()
        .setBaseUri(cfg.bookingUrl())
        .setBasePath("/booking")
        .setContentType(ContentType.JSON)
        .build();
    private final        BookingApiClient     bookingApiClient   = new BookingApiClient();
    private final        BookingSteps         bookingSteps       = new BookingSteps();

    private static Booking.BookingBuilder validBooking() {
        return Booking.builder()
            .firstname(faker.name().firstName())
            .lastname(faker.name().lastName())
            .totalprice(faker.number().numberBetween(1, 1000))
            .depositpaid(faker.bool().bool())
            .bookingdates(new BookingDates("2027-01-01", "2028-01-01"))
            .additionalneeds(faker.lorem().sentence());
    }

    static Stream<Arguments> missingRequiredFields() {
        return Stream.of(
            arguments(named("missing firstname", validBooking().firstname(null).build())),
            arguments(named("missing lastname", validBooking().lastname(null).build())),
            arguments(named("missing totalprice", validBooking().totalprice(null).build())),
            arguments(named("missing depositpaid", validBooking().depositpaid(null).build())),
            arguments(named("missing bookingdates", validBooking().bookingdates(null).build())),
            arguments(named("empty body", Booking.builder().build()))
        );
    }

    private ValidatableResponse createBooking(Object body) {
        return given()
            .spec(bookingRequestSpec)
            .body(body)
            .when().post()
            .then();
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void validBookingReturnsCreatedBooking() {
        Booking booking = validBooking().build();

        BookingResponse res = createBooking(booking)
            .statusCode(200)
            .extract().as(BookingResponse.class);

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(booking, res.getBooking());
    }

    // API возвращает 500, а не 400, если нет обязательного поля
    @ParameterizedTest(name = "{0}")
    @MethodSource("missingRequiredFields")
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void missingRequiredFieldReturnsError(Booking booking) {
        Response response = createBooking(booking).extract().response();
        assertThat(response.getStatusCode()).as("Invalid booking response status").isEqualTo(500);
    }

    @Test
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void negativePriceReturnsCreatedBooking() {
        Booking booking = validBooking().totalprice(-9999).build();

        BookingResponse res = createBooking(booking)
            .statusCode(200)
            .extract().as(BookingResponse.class);

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(booking, res.getBooking());
    }

    // API сохраняет невалидные даты как "0NaN-aN-aN"
    @ParameterizedTest(name = "{0}")
    @CsvSource({
        "invalid checkin date, not-a-date, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "invalid checkout date, 2027-01-01, not-a-date, 2027-01-01, 0NaN-aN-aN",
        "invalid checkin and checkout dates, not-a-date, not-a-date, 0NaN-aN-aN, 0NaN-aN-aN",
        "checkin after checkout, 2028-01-01, 2027-01-01, 2028-01-01, 2027-01-01",
        "far-future checkin, 9999-01-01, 2028-01-01, 9999-01-01, 2028-01-01",
        "nonexistent checkin month, 2027-13-01, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "nonexistent checkin day, 2027-12-32, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "far-future checkout, 2027-01-01, 9999-01-01, 2027-01-01, 9999-01-01",
        "nonexistent checkout month, 2027-01-01, 2028-33-01, 2027-01-01, 0NaN-aN-aN",
        "nonexistent checkout day, 2027-01-01, 2028-01-89, 2027-01-01, 0NaN-aN-aN"
    })
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void invalidDatesReturnCreatedBooking(
        String caseName, String checkin, String checkout, String expectedCheckin, String expectedCheckout
    ) {
        Booking booking = validBooking()
            .bookingdates(new BookingDates(checkin, checkout))
            .build();

        BookingResponse res = createBooking(booking)
            .statusCode(200)
            .extract().as(BookingResponse.class);

        Booking expected = Booking.builder()
            .firstname(booking.getFirstname())
            .lastname(booking.getLastname())
            .totalprice(booking.getTotalprice())
            .depositpaid(booking.getDepositpaid())
            .bookingdates(new BookingDates(expectedCheckin, expectedCheckout))
            .additionalneeds(booking.getAdditionalneeds())
            .build();

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(expected, res.getBooking());
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void validBookingUpdateReturnsUpdatedBooking() {
        Response createdBooking = bookingApiClient.createBooking(validBooking().build());
        assertThat(createdBooking.getStatusCode()).as("Booking creation status").isEqualTo(200);

        Integer bookingId = createdBooking.as(BookingResponse.class).getBookingid();
        assertThat(bookingId).as("Created booking ID").isPositive();

        Response authResponse = bookingApiClient.authenticate();
        assertThat(authResponse.getStatusCode()).as("Authentication status").isEqualTo(200);
        String token = authResponse.as(AuthResponse.class).getToken();
        assertThat(token).as("Authentication token").isNotBlank();

        Booking  newBookingData = validBooking().build();
        Response updatedBooking = bookingApiClient.updateBooking(bookingId, newBookingData, token);

        assertThat(updatedBooking.getStatusCode()).as("Booking update status").isEqualTo(200);
        bookingSteps.verifyBookingEquals(newBookingData, updatedBooking.as(Booking.class));

        Response fetchedBooking = bookingApiClient.getBooking(bookingId);
        assertThat(fetchedBooking.getStatusCode()).as("Fetched booking status").isEqualTo(200);
        bookingSteps.verifyBookingEquals(newBookingData, fetchedBooking.as(Booking.class));
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void partialUpdateReturnsUpdatedBooking() {
        Booking  initialBooking = validBooking().build();
        Response createdBooking = bookingApiClient.createBooking(initialBooking);
        assertThat(createdBooking.getStatusCode()).isEqualTo(200);
        int bookingId = createdBooking.as(BookingResponse.class).getBookingid();

        Response authResponse = bookingApiClient.authenticate();
        assertThat(authResponse.getStatusCode()).as("Authentication status").isEqualTo(200);
        String token = authResponse.as(AuthResponse.class).getToken();
        assertThat(token).as("Authentication token").isNotBlank();

        Booking partialUpdate = Booking.builder()
            .firstname("Barak")
            .lastname("Obama")
            .additionalneeds("Guy Fawkes mask")
            .build();

        Response updatedBooking = bookingApiClient.partiallyUpdateBooking(bookingId, partialUpdate, token);

        assertThat(updatedBooking.getStatusCode()).isEqualTo(200);

        Booking expectedBooking = Booking.builder()
            .firstname(partialUpdate.getFirstname())
            .lastname(partialUpdate.getLastname())
            .totalprice(initialBooking.getTotalprice())
            .depositpaid(initialBooking.getDepositpaid())
            .bookingdates(initialBooking.getBookingdates())
            .additionalneeds(partialUpdate.getAdditionalneeds())
            .build();

        Response fetchedBooking = bookingApiClient.getBooking(bookingId);
        assertThat(fetchedBooking.getStatusCode()).isEqualTo(200);
        bookingSteps.verifyBookingEquals(expectedBooking, fetchedBooking.as(Booking.class));
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void deleteBookingTest() {
        Response booking = bookingApiClient.createBooking(validBooking().build());
        assertThat(booking.getStatusCode()).isEqualTo(200);

        Response deleteBookingRes = bookingApiClient.deleteBooking(booking.as(BookingResponse.class).getBookingid());
        assertThat(deleteBookingRes.getStatusCode()).isEqualTo(201);

        Response getBookingRes = bookingApiClient.getBooking(booking.as(BookingResponse.class).getBookingid());
        assertThat(getBookingRes.getStatusCode()).isEqualTo(404);
    }
}
