package com.github.zh00k.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.zh00k.util.AttachmentManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.github.zh00k.config.Config.WEBSHOP_URL;
import static com.github.zh00k.config.Config.setBrowserOptions;


public abstract class BaseTest {
    @BeforeAll
    protected static void beforeAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = WEBSHOP_URL;

        setBrowserOptions();
    }

    @AfterEach
    protected void afterEach() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }

        String sessionId = Selenide.sessionId().toString();
        AttachmentManager.screenshot();

        closeWebDriver();

        if (Configuration.remote != null) {
            AttachmentManager.video(sessionId);
        }
    }
}
