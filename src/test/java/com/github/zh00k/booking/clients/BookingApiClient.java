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
    private static final BookingConfig cfg = BookingApiConfig.getBookingConfig();

    public Response authenticate() {
        return given()
            .baseUri(cfg.bookingUrl())
            .contentType(ContentType.JSON)
            .body(new AuthRequest(cfg.username(), cfg.password()))
            .when()
            .post("/auth")
            .then()
            .extract()
            .response();
    }

    public String getToken() {
        return given()
            .baseUri(cfg.bookingUrl())
            .contentType(ContentType.JSON)
            .body(new AuthRequest(cfg.username(), cfg.password()))
            .when().post("/auth")
            .then()
            .extract().as(AuthResponse.class)
            .getToken();
    }

    public Response createBooking(Booking booking) {
        return given()
            .baseUri(cfg.bookingUrl())
            .contentType(ContentType.JSON)
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .extract()
            .response();
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        return given()
            .baseUri(cfg.bookingUrl())
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .body(booking)
            .pathParam("bookingId", bookingId)
            .when()
            .put("/booking/{bookingId}")
            .then()
            .extract()
            .response();
    }

    public Response getBooking(int bookingId) {
        return given()
            .baseUri(cfg.bookingUrl())
            .contentType(ContentType.JSON)
            .pathParam("id", bookingId)
            .when()
            .get("/booking/{id}")
            .then()
            .extract()
            .response();
    }

    public Response partiallyUpdateBooking(int bookingId, Booking booking, String token) {
        return given()
            .baseUri(cfg.bookingUrl())
            .cookie("token", token)
            .contentType(ContentType.JSON)
            .body(booking)
            .pathParam("bookingId", bookingId)
            .when()
            .patch("/booking/{bookingId}")
            .then()
            .extract()
            .response();
    }

    public Response deleteBooking(int bookingId) {
        return given()
            .baseUri(cfg.bookingUrl())
            .cookie("token", getToken())
            .contentType(ContentType.JSON)
            .pathParam("bookingId", bookingId)
            .delete("/booking/{bookingId}")
            .then()
            .extract()
            .response();
    }
}
