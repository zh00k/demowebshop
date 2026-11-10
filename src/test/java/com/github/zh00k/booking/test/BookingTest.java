package com.github.zh00k.booking.test;

import com.github.zh00k.booking.clients.BookingApiClient;
import com.github.zh00k.booking.config.BookingApiConfig;
import com.github.zh00k.booking.config.BookingConfig;
import com.github.zh00k.booking.dto.Booking;
import com.github.zh00k.booking.dto.BookingDates;
import com.github.zh00k.booking.dto.BookingResponse;
import com.github.zh00k.booking.steps.BookingSteps;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
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
    private static final Faker            faker            = new Faker();
    private static final BookingConfig    cfg              = BookingApiConfig.getBookingConfig();
    private final        BookingApiClient bookingApiClient = new BookingApiClient();
    private final        BookingSteps     bookingSteps     = new BookingSteps();

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
            .baseUri(cfg.bookingUrl())
            .basePath("/booking")
            .contentType(ContentType.JSON)
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
        createBooking(booking).statusCode(500);
    }

    @Test
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void negativePriceReturnsCreatedBooking() {
        Booking booking = validBooking().totalprice(-9999).build();

        BookingResponse res = createBooking(booking)
            .statusCode(200)
            .extract().as(BookingResponse.class);

        assertThat(res.getBookingid()).isNotNull();
        assertThat(res.getBooking()).isEqualTo(booking);
    }

    // API сохраняет невалидные даты как "0NaN-aN-aN"
    @ParameterizedTest(name = "{0}")
    @CsvSource({
        "invalid checkin date, not-a-date, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "invalid checkout date, 2027-01-01, not-a-date, 2027-01-01, 0NaN-aN-aN",
        "invalid checkin and checkout dates, not-a-date, not-a-date, 0NaN-aN-aN, 0NaN-aN-aN",
        "checkin after checkout, 2028-01-01, 2027-01-01, 2028-01-01, 2027-01-01",
        "nonexistent checkin year, 9999-13-01, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "nonexistent checkin month, 2027-13-01, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "nonexistent checkin day, 2027-12-32, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        // У checkout года по какой-то причине нет валидации...
        "nonexistent checkout year, 9999-01-01, 2028-01-01, 9999-01-01, 2028-01-01",
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
        assertThat(res.getBooking()).isEqualTo(expected);
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void validBookingUpdateReturnsUpdatedBooking() {
        BookingResponse booking = bookingApiClient
            .createBooking(validBooking().build())
            .as(BookingResponse.class);

        Booking newBookingData = validBooking().build();

        Response updatedBooking = bookingApiClient.updateBooking(
            booking.getBookingid(),
            newBookingData
        );

        assertThat(updatedBooking.getStatusCode()).isEqualTo(200);
        bookingSteps.verifyBookingEquals(newBookingData, updatedBooking.as(Booking.class));
    }
}
