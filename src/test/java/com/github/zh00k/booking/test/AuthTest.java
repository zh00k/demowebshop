package com.github.zh00k.booking.test;

import com.github.zh00k.booking.dto.AuthRequest;
import com.github.zh00k.booking.dto.AuthResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

// API возвращает 200 даже при неверных данных
public class AuthTest extends BaseApiTest {
    private static final String BOOKING_AUTH_URL = "https://restful-booker.herokuapp.com/auth";
    private static final String BAD_CREDENTIALS  = "Bad credentials";
    private static final String USERNAME         = "admin";
    private static final String PASSWORD         = "password123";

    // Можно сделать через Record, но кода будет больше...
    static Stream<Arguments> invalidAuthRequests() {
        return Stream.of(
            arguments("wrong password", new AuthRequest(USERNAME, "some-password"), BAD_CREDENTIALS),
            arguments("wrong username", new AuthRequest("lololoshka", PASSWORD), BAD_CREDENTIALS),
            arguments("null password", new AuthRequest(USERNAME, null), BAD_CREDENTIALS),
            arguments("null username", new AuthRequest(null, PASSWORD), BAD_CREDENTIALS),
            arguments("empty body", "{}", BAD_CREDENTIALS)
        );
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive"), @Tag("Authorization")})
    void validCredentialsReturnToken() {
        AuthResponse res = given()
            .contentType(ContentType.JSON)
            .body(new AuthRequest(USERNAME, PASSWORD))
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNotBlank();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidAuthRequests")
    @Tags({@Tag("API"), @Tag("Negative"), @Tag("Authorization")})
    void invalidAuthRequestReturnsReason(String caseName, Object body, String expectedReason) {
        AuthResponse res = given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNull();
        assertThat(res.getReason()).isEqualTo(expectedReason);
    }

    @Test
    @Tags({@Tag("API"), @Tag("Negative"), @Tag("Authorization")})
    void noBodyReturnsReason() {
        AuthResponse res = given()
            .contentType(ContentType.JSON)
            .when().post(BOOKING_AUTH_URL)
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNull();
        assertThat(res.getReason()).isEqualTo(BAD_CREDENTIALS);
    }
}
