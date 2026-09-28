package com.github.zh00k.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.*;
import static com.github.zh00k.config.Config.WEBSHOP_URL;

public abstract class BaseTest {
    @BeforeAll
    protected static void beforeAll() {
        Configuration.baseUrl = WEBSHOP_URL;
    }

    @AfterEach
    protected void afterEach() {
        closeWebDriver();
    }
}
