package com.github.zh00k.booking.test;

import org.junit.jupiter.api.BeforeAll;

import static com.github.zh00k.booking.util.RestAssuredSpec.setupRestAssured;

abstract class BaseApiTest {
    @BeforeAll
    static void setUpFilters() {
        setupRestAssured();
    }
}
