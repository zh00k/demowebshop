package com.github.zh00k.steps;

import com.github.zh00k.data.DataGenerator;
import com.github.zh00k.data.User;
import com.github.zh00k.pages.RegistrationPage;

public class UserSteps {

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
