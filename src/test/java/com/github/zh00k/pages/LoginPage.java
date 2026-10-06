package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;

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

    public LoginPage fillEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public LoginPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public LoginPage clickRememberMeCheckbox() {
        rememberMeCheckbox.click();
        return this;
    }

    public <P> P clickLoginButton(Class<P> nextPage) {
        loginButton.click();
        return page(nextPage);
    }

    public LoginPage shouldShowEmailValidationError() {
        emailValidationErrorField.shouldHave(exactText(emailValidationErrorText));
        return this;
    }
}
