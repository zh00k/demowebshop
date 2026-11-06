package com.github.zh00k.booking.test;

import com.github.zh00k.booking.dto.AuthRequest;
import com.github.zh00k.booking.dto.AuthResponse;
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
import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest {
    private static final String BOOKING_AUTH_URL = "https://restful-booker.herokuapp.com/auth";

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive"), @Tag("Authorization")})
    void successfulAuthTest() {
        String username = "admin";
        String password = "password123";

        AuthResponse res = given()
            .contentType(ContentType.JSON)
            .body(new AuthRequest(username, password))
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNotNull();
    }
}
