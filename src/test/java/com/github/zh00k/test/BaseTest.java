package com.github.zh00k.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.github.zh00k.config.Config.WEBSHOP_URL;

public abstract class BaseTest {
    @BeforeAll
    protected static void beforeAll() {
        Configuration.baseUrl = WEBSHOP_URL;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    protected void afterEach() {
        closeWebDriver();
    }
}
