package com.github.zh00k.booking.clients;

import com.github.zh00k.booking.config.BookingApiConfig;
import com.github.zh00k.booking.config.BookingConfig;
import com.github.zh00k.booking.dto.AuthRequest;
import com.github.zh00k.booking.dto.AuthResponse;
import com.github.zh00k.booking.dto.Booking;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingApiClient {
    private static final BookingConfig cfg      = BookingApiConfig.getBookingConfig();
    private static final String        BASE_URL = "https://restful-booker.herokuapp.com";

    public String getToken() {
        return given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(new AuthRequest(cfg.username(), cfg.password()))
            .when().post("/auth")
            .then()
            .extract().as(AuthResponse.class)
            .getToken();
    }

    public Response createBooking(Booking booking) {
        return given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .extract()
            .response();
    }

    public Response updateBooking(int bookingId, Booking booking) {
        return given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .cookie("token", getToken())
            .body(booking)
            .pathParam("bookingId", bookingId)
            .when()
            .put("/booking/{bookingId}")
            .then()
            .extract()
            .response();
    }
}
