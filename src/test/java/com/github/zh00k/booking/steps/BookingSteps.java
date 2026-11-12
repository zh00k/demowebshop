package com.github.zh00k.booking.steps;

import com.github.zh00k.booking.dto.Booking;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingSteps {
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
}
