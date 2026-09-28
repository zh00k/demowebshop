package com.github.zh00k.test;

import com.github.zh00k.data.User;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseTest {
    private final UserSteps   userSteps   = new UserSteps();
    private final WelcomePage welcomePage = new WelcomePage();

    @Test
    @DisplayName("Успешный вход с валидными данными")
    void successfulLogin() {
        User user = userSteps.registerUser();

        welcomePage
                .header()
                .clickLogOut()
                .shouldBeLoggedOut()
                .clickLogin()
                .fillEmail(user.email())
                .fillPassword(user.password())
                .clickRememberMeCheckbox()
                .clickLoginButton()
                .header()
                .shouldBeLoggedInAs(user.email());
    }
}
