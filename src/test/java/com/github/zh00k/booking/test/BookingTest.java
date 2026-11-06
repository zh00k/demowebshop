package com.github.zh00k.booking.test;

import com.github.zh00k.booking.dto.Booking;
import com.github.zh00k.booking.dto.BookingDates;
import com.github.zh00k.booking.dto.BookingResponse;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BookingTest {
    private static final String BOOKING_URL = "https://restful-booker.herokuapp.com/booking";

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive"), @Tag("Booking")})
    void successfulBookingTest() {
        Booking bookingBody = Booking.builder()
            .firstname("Barak")
            .lastname("Obama")
            .totalprice(666)
            .depositpaid(false)
            .bookingdates(new BookingDates("2027-01-01", "2028-01-01"))
            .additionalneeds("toilet paper")
            .build();

        BookingResponse res = given()
            .contentType(ContentType.JSON)
            .body(bookingBody)
            .when().post(BOOKING_URL)
            .then().statusCode(200)
            .extract().as(BookingResponse.class);
    }
}
