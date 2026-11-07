package com.github.zh00k.booking.test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

abstract class BaseApiTest {
    @BeforeAll
    static void setUpFilters() {
        RestAssured.replaceFiltersWith(
            new RequestLoggingFilter(),
            new ResponseLoggingFilter(),
            new AllureRestAssured()
        );
    }
}
