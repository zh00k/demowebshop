package com.github.zh00k.booking.test;

import com.github.zh00k.booking.clients.BookingApiClient;
import com.github.zh00k.booking.dto.AuthResponse;
import com.github.zh00k.booking.dto.Booking;
import com.github.zh00k.booking.dto.BookingDates;
import com.github.zh00k.booking.dto.BookingResponse;
import com.github.zh00k.booking.steps.BookingSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static com.github.zh00k.booking.steps.BookingSteps.validBooking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

public class BookingTest extends BaseApiTest {
    private final        BookingApiClient bookingApiClient = new BookingApiClient();
    private final        BookingSteps     bookingSteps     = new BookingSteps();

    static Stream<Arguments> missingRequiredFields() {
        return Stream.of(arguments(named("missing firstname", validBooking().firstname(null).build())),
            arguments(named("missing lastname", validBooking().lastname(null).build())),
            arguments(named("missing totalprice", validBooking().totalprice(null).build())),
            arguments(named("missing depositpaid", validBooking().depositpaid(null).build())),
            arguments(named("missing bookingdates", validBooking().bookingdates(null).build())),
            arguments(named("empty body", Booking.builder().build())));
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void validBookingReturnsCreatedBooking() {
        Booking booking = validBooking().build();

        BookingResponse res = bookingSteps.createBooking(booking);

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(booking, res.getBooking());
    }

    // API возвращает 500, а не 400, если нет обязательного поля
    @ParameterizedTest(name = "{0}")
    @MethodSource("missingRequiredFields")
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void missingRequiredFieldReturnsError(Booking booking) {
        Response response = bookingApiClient.createBooking(booking);
        assertThat(response.getStatusCode()).as("Invalid booking response status").isEqualTo(500);
    }

    @Test
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void negativePriceReturnsCreatedBooking() {
        Booking booking = validBooking().totalprice(-9999).build();

        BookingResponse res = bookingSteps.createBooking(booking);

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(booking, res.getBooking());
    }

    // API сохраняет невалидные даты как "0NaN-aN-aN"
    @ParameterizedTest(name = "{0}")
    @CsvSource({"invalid checkin date, not-a-date, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "invalid checkout date, 2027-01-01, not-a-date, 2027-01-01, 0NaN-aN-aN",
        "invalid checkin and checkout dates, not-a-date, not-a-date, 0NaN-aN-aN, " + "0NaN-aN-aN",
        "checkin after checkout, 2028-01-01, 2027-01-01, 2028-01-01, 2027-01-01",
        "far-future checkin, 9999-01-01, 2028-01-01, 9999-01-01, 2028-01-01",
        "nonexistent checkin month, 2027-13-01, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "nonexistent checkin day, 2027-12-32, 2028-01-01, 0NaN-aN-aN, 2028-01-01",
        "far-future checkout, 2027-01-01, 9999-01-01, 2027-01-01, 9999-01-01",
        "nonexistent checkout month, 2027-01-01, 2028-33-01, 2027-01-01, 0NaN-aN-aN",
        "nonexistent checkout day, 2027-01-01, 2028-01-89, 2027-01-01, 0NaN-aN-aN"})
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Booking")})
    void invalidDatesReturnCreatedBooking(String caseName, String checkin, String checkout,
                                          String expectedCheckin, String expectedCheckout) {
        Booking booking = validBooking().bookingdates(new BookingDates(checkin, checkout)).build();

        BookingResponse res = bookingSteps.createBooking(booking);

        Booking expected = Booking.builder().firstname(booking.getFirstname()).lastname(
            booking.getLastname()).totalprice(booking.getTotalprice()).depositpaid(
            booking.getDepositpaid()).bookingdates(
            new BookingDates(expectedCheckin, expectedCheckout)).additionalneeds(
            booking.getAdditionalneeds()).build();

        assertThat(res.getBookingid()).isNotNull();
        bookingSteps.verifyBookingEquals(expected, res.getBooking());
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void validBookingUpdateReturnsUpdatedBooking() {
        BookingResponse createdBooking = bookingSteps.createBooking();

        Integer bookingId = createdBooking.getBookingid();
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
        Booking         initialBooking = validBooking().build();
        BookingResponse createdBooking = bookingSteps.createBooking(initialBooking);
        int             bookingId      = createdBooking.getBookingid();

        Response authResponse = bookingApiClient.authenticate();
        assertThat(authResponse.getStatusCode()).as("Authentication status").isEqualTo(200);
        String token = authResponse.as(AuthResponse.class).getToken();
        assertThat(token).as("Authentication token").isNotBlank();

        Booking partialUpdate = Booking.builder().firstname("Barak").lastname("Obama").additionalneeds(
            "Guy Fawkes mask").build();

        Response updatedBooking = bookingApiClient.partiallyUpdateBooking(bookingId, partialUpdate, token);

        assertThat(updatedBooking.getStatusCode()).isEqualTo(200);

        Booking expectedBooking = Booking.builder().firstname(partialUpdate.getFirstname()).lastname(
            partialUpdate.getLastname()).totalprice(initialBooking.getTotalprice()).depositpaid(
            initialBooking.getDepositpaid()).bookingdates(initialBooking.getBookingdates()).additionalneeds(
            partialUpdate.getAdditionalneeds()).build();

        Response fetchedBooking = bookingApiClient.getBooking(bookingId);
        assertThat(fetchedBooking.getStatusCode()).isEqualTo(200);
        bookingSteps.verifyBookingEquals(expectedBooking, fetchedBooking.as(Booking.class));
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Booking")})
    void deleteBookingTest() {
        Integer bookingId = bookingSteps.createBooking().getBookingid();

        Response deleteBookingRes = bookingApiClient.deleteBooking(bookingId);
        assertThat(deleteBookingRes.getStatusCode()).isEqualTo(201);

        Response getBookingRes = bookingApiClient.getBooking(bookingId);
        assertThat(getBookingRes.getStatusCode()).isEqualTo(404);
    }
}
