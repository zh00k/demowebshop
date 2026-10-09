package com.github.zh00k.test;

import com.github.zh00k.data.DataGenerator;
import com.github.zh00k.data.User;
import com.github.zh00k.pages.WelcomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.zh00k.config.Config.KAITEN_ALLURE_URL;
import static com.github.zh00k.config.Config.KAITEN_JAVA_BASICS_URL;

@Epic("Demo Web Shop")
@Feature("Регистрация")
public class RegistrationTest extends BaseTest {
    private final WelcomePage welcomePage = new WelcomePage();

    @Test
    @DisplayName("Регистрация нового пользователя со всеми заполненными полями")
    @Tag("positive")
    @Owner("zh00k")
    @Link(name = "Основы Java", url = KAITEN_JAVA_BASICS_URL)
    @Link(name = "Allure", url = KAITEN_ALLURE_URL)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Регистрация нового пользователя")
    void successfulRegistrationWithValidData() {
        User user = DataGenerator.randomUser();

        welcomePage
                .open()
                .shouldHaveCorrectTitle()
                .header()
                .clickRegister()
                .shouldBeOpened()
                .selectGender(user.gender())
                .fillFirstName(user.firstName())
                .fillLastName(user.lastName())
                .fillEmail(user.email())
                .fillPassword(user.password())
                .fillConfirmPassword(user.password())
                .clickRegister()
                .shouldBeRegistered(user.email());
    }
}
