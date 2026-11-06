package com.github.zh00k.booking.test;

import com.github.zh00k.booking.dto.AuthRequest;
import com.github.zh00k.booking.dto.AuthResponse;
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
    private static final String BOOKING_AUTH_URL = "https://restful-booker.herokuapp.com/auth";

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive")})
    void authTest() {
        String username = "admin";
        String password = "password123";

        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);

        String body = "{" +
            "    \"username\" : \"" + username + "\"," +
            "    \"password\" : \"" + password + "\"" +
            "}";

        AuthResponse res = given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        System.out.println("\nTOKEN ---> " + res.getToken() + "\n");
    }
}
