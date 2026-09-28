package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.data.Gender;

import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage<RegistrationPage> {

    private final SelenideElement genderMale   = $("#gender-male");
    private final SelenideElement genderFemale = $("#gender-female");

    private final SelenideElement firstNameInput       = $("#FirstName");
    private final SelenideElement lastNameInput        = $("#LastName");
    private final SelenideElement emailInput           = $("#Email");
    private final SelenideElement passwordInput        = $("#Password");
    private final SelenideElement confirmPasswordInput = $("#ConfirmPassword");

    private final SelenideElement registerButton = $("#register-button");

    public RegistrationPage() {
        super("/register", TITLE_PREFIX + "Register");
    }

    public RegistrationPage selectGender(Gender gender) {
        SelenideElement radio = switch (gender) {
            case MALE -> genderMale;
            case FEMALE -> genderFemale;
        };

        radio.click();
        return this;
    }

    public RegistrationPage fillFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }

    public RegistrationPage fillLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    public RegistrationPage fillEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public RegistrationPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegistrationPage fillConfirmPassword(String password) {
        confirmPasswordInput.setValue(password);
        return this;
    }

    public RegisterResultPage clickRegister() {
        registerButton.click();
        return new RegisterResultPage();
    }
}
