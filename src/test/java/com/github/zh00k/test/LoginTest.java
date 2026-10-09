package com.github.zh00k.test;

import com.github.zh00k.data.User;
import com.github.zh00k.pages.LoginPage;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.zh00k.config.Config.KAITEN_ALLURE_URL;
import static com.github.zh00k.config.Config.KAITEN_JAVA_BASICS_URL;
import static com.github.zh00k.config.Config.KAITEN_JUNIT_URL;

@Epic("Demo Web Shop")
@Feature("Авторизация")
public class LoginTest extends BaseTest {
    private final UserSteps   userSteps   = new UserSteps();
    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage   loginPage   = new LoginPage();

    @Test
    @DisplayName("Вход зарегистрированного пользователя по email и паролю")
    @Tag("positive")
    @Owner("zh00k")
    @Link(name = "Основы Java", url = KAITEN_JAVA_BASICS_URL)
    @Link(name = "Allure", url = KAITEN_ALLURE_URL)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Успешный вход")
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
    @Owner("zh00k")
    @Link(name = "JUnit-аннотации", url = KAITEN_JUNIT_URL)
    @Link(name = "Allure", url = KAITEN_ALLURE_URL)
    @Severity(SeverityLevel.NORMAL)
    @Story("Вход с невалидными данными")
    void invalidEmailLogin(String email) {
        loginPage
                .open()
                .fillEmail(email)
                .fillPassword("1234")
                .clickLoginButton(LoginPage.class)
                .shouldShowEmailValidationError();
    }
}
