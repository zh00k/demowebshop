package com.github.zh00k.test;

import com.github.zh00k.data.User;
import com.github.zh00k.pages.LoginPage;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LoginTest extends BaseTest {
    private final UserSteps   userSteps   = new UserSteps();
    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage   loginPage   = new LoginPage();

    @Test
    @DisplayName("Вход зарегистрированного пользователя по email и паролю")
    @Tag("positive")
    void successfulLogin() {
        User user = userSteps.registerUser();

        welcomePage
                .header()
                .clickLogOut()
                .shouldBeLoggedOut()
                .clickLogin()
                .fillEmail(user.email())
                .fillPassword(user.password())
                .clickLoginButton(WelcomePage.class)
                .header()
                .shouldBeLoggedInAs(user.email());
    }

    @ParameterizedTest(name = "Невалидный email: {0}")
    @ValueSource(strings = {"plainaddress", "user@", "@mail.ru", "user@mail", "user@@email.ru"})
    @Tag("negative")
    @DisplayName("Ошибка валидации при невалидном email")
    void invalidEmailLogin(String email) {
        loginPage
                .open()
                .fillEmail(email)
                .fillPassword("1234")
                .clickLoginButton(LoginPage.class)
                .shouldShowEmailValidationError();
    }
}
