package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement emailInput         = $("input#Email");
    private final SelenideElement passwordInput      = $("input#Password");
    private final SelenideElement rememberMeCheckbox = $("input#RememberMe");
    private final SelenideElement loginButton        = $("input.login-button");

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

    public WelcomePage clickLoginButton() {
        loginButton.click();
        return new WelcomePage();
    }
}
