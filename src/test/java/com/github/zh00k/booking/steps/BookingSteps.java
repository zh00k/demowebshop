package com.github.zh00k.booking.steps;

import com.github.zh00k.booking.clients.BookingApiClient;
import com.github.zh00k.booking.dto.Booking;
import com.github.zh00k.booking.dto.BookingDates;
import com.github.zh00k.booking.dto.BookingResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import net.datafaker.Faker;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingSteps {
    private static final Faker            faker            = new Faker();
    private final        BookingApiClient bookingApiClient = new BookingApiClient();

    public static Booking.BookingBuilder validBooking() {
        return Booking.builder().firstname(faker.name().firstName()).lastname(
            faker.name().lastName()).totalprice(faker.number().numberBetween(1, 1000)).depositpaid(
            faker.bool().bool()).bookingdates(new BookingDates("2026-01-01", "2028-01-01")).additionalneeds(
            faker.lorem().sentence());
    }


    @Step("Проверка всех полей в ответе")
    public void verifyBookingEquals(Booking expected, Booking actual) {
        assertAll(
            () -> assertEquals(
                expected.getFirstname(), actual.getFirstname(), "Booking firstname should match"
            ),
            () -> assertEquals(
                expected.getLastname(), actual.getLastname(), "Booking lastname should match"
            ),
            () -> assertEquals(
                expected.getTotalprice(), actual.getTotalprice(), "Booking total price should match"
            ),
            () -> assertEquals(
                expected.getDepositpaid(), actual.getDepositpaid(), "Booking deposit status should match"
            ),
            () -> assertAll("Booking dates should match",
                () -> assertEquals(
                    expected.getBookingdates().getCheckin(),
                    actual.getBookingdates().getCheckin(), "Booking check-in date should match"
                ),
                () -> assertEquals(
                    expected.getBookingdates().getCheckout(),
                    actual.getBookingdates().getCheckout(), "Booking check-out date should match"
                )
            ),
            () -> assertEquals(
                expected.getAdditionalneeds(), actual.getAdditionalneeds(),
                "Booking additional needs should match"
            )
        );
    }

    @Step("Создание букинга с готовыми данными")
    public BookingResponse createBooking(Booking booking) {
        Response createRes = bookingApiClient.createBooking(booking);

        step("Проверить, что статус код = 200", () ->
            assertThat(createRes.getStatusCode()).isEqualTo(200));

        return createRes.as(BookingResponse.class);
    }

    @Step("Создание букинга со сгенерированными данными")
    public BookingResponse createBooking() {
        return createBooking(validBooking().build());
    }
}
