package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement emailInput         = $("input#Email");
    private final SelenideElement passwordInput      = $("input#Password");
    private final SelenideElement rememberMeCheckbox = $("input#RememberMe");
    private final SelenideElement loginButton        = $("input.login-button");

    private final SelenideElement emailValidationErrorField = $("span.field-validation-error[data-valmsg-for='Email']");

    private final String emailValidationErrorText = "Please enter a valid email address.";

    public LoginPage() {
        super("/login", TITLE_PREFIX + "Login");
    }

    @Step("Ввести email «{email}»")
    public LoginPage fillEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Отметить чекбокс «Remember me»")
    public LoginPage clickRememberMeCheckbox() {
        rememberMeCheckbox.click();
        return this;
    }

    @Step("Нажать кнопку «Log in»")
    public <P> P clickLoginButton(Class<P> nextPage) {
        loginButton.click();
        return page(nextPage);
    }

    @Step("Проверить ошибку валидации email")
    public LoginPage shouldShowEmailValidationError() {
        emailValidationErrorField.shouldHave(exactText(emailValidationErrorText));
        return this;
    }
}
