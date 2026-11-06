package com.github.zh00k.booking.test;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class BookingTest {
    private static final String BOOKING_AUTH_URL = "https://restful-booker.herokuapp.com/auth";

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive")})
    void authTest() {
        String user     = "admin";
        String password = "password123";

        String body = "{" +
            "    \"username\" : \"" + user + "\"," +
            "    \"password\" : \"" + password + "\"" +
            "}";

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .body("token", notNullValue());
    }
}
