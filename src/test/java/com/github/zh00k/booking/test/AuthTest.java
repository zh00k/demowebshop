package com.github.zh00k.booking.test;

import com.github.zh00k.booking.config.BookingApiConfig;
import com.github.zh00k.booking.config.BookingConfig;
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
    private static final BookingConfig cfg              = BookingApiConfig.getBookingConfig();
    private static final String        BOOKING_BASE_URL = BookingApiConfig.getBookingConfig().bookingUrl();
    private static final String        BAD_CREDENTIALS  = "Bad credentials";

    // Можно сделать через Record, но кода будет больше...
    static Stream<Arguments> invalidAuthRequests() {
        return Stream.of(
            arguments("wrong password", new AuthRequest(cfg.username(), "some-password"), BAD_CREDENTIALS),
            arguments("wrong username", new AuthRequest("lololoshka", cfg.password()), BAD_CREDENTIALS),
            arguments("empty password", new AuthRequest(cfg.username(), ""), BAD_CREDENTIALS),
            arguments("empty username", new AuthRequest("", cfg.password()), BAD_CREDENTIALS),
            arguments("null password", new AuthRequest(cfg.username(), null), BAD_CREDENTIALS),
            arguments("null username", new AuthRequest(null, cfg.password()), BAD_CREDENTIALS),
            arguments("empty body", new AuthRequest(), BAD_CREDENTIALS)
        );
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive"), @Tag("Authorization")})
    void validCredentialsReturnToken() {
        AuthResponse res = given()
            .baseUri(BOOKING_BASE_URL)
            .contentType(ContentType.JSON)
            .body(new AuthRequest(cfg.username(), cfg.password()))
            .when().post("/auth")
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNotBlank();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidAuthRequests")
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Authorization")})
    void invalidAuthRequestReturnsReason(String caseName, AuthRequest body, String expectedReason) {
        AuthResponse res = given()
            .baseUri(BOOKING_BASE_URL)
            .contentType(ContentType.JSON)
            .body(body)
            .when().post("/auth")
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNull();
        assertThat(res.getReason()).isEqualTo(expectedReason);
    }

    @Test
    @Tags({@Tag("API"), @Tag("negative"), @Tag("Authorization")})
    void noBodyReturnsReason() {
        AuthResponse res = given()
            .baseUri(BOOKING_BASE_URL)
            .contentType(ContentType.JSON)
            .when().post("/auth")
            .then().statusCode(200)
            .extract().as(AuthResponse.class);

        assertThat(res.getToken()).isNull();
        assertThat(res.getReason()).isEqualTo(BAD_CREDENTIALS);
    }
}
