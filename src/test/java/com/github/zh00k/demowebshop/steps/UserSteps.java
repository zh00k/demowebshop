package com.github.zh00k.demowebshop.steps;

import com.github.zh00k.demowebshop.data.DataGenerator;
import com.github.zh00k.demowebshop.data.User;
import com.github.zh00k.demowebshop.pages.RegistrationPage;
import io.qameta.allure.Step;

public class UserSteps {

    @Step("Зарегистрировать нового пользователя")
    public User registerUser() {
        RegistrationPage registrationPage = new RegistrationPage();
        User             user             = DataGenerator.randomUser();

        registrationPage
            .open()
            .selectGender(user.gender())
            .fillFirstName(user.firstName())
            .fillLastName(user.lastName())
            .fillEmail(user.email())
            .fillPassword(user.password())
            .fillConfirmPassword(user.password())
            .clickRegister()
            .shouldBeRegistered(user.email());

        return user;
    }
}
