package com.github.zh00k.test;

import com.github.zh00k.data.DataGenerator;
import com.github.zh00k.data.User;
import com.github.zh00k.pages.WelcomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest extends BaseTest {
    private final WelcomePage welcomePage = new WelcomePage();

    @Test
    @DisplayName("Успешная регистрация с валидными данными")
    void successfulRegistrationWithValidData() {
        User user = DataGenerator.randomUser();

        welcomePage
                .open()
                .shouldHaveCorrectTitle()
                .header()
                .clickRegister()
                .shouldBeOpened()
                .selectGender(DataGenerator.gender())
                .fillFirstName(DataGenerator.firstName())
                .fillLastName(DataGenerator.lastName())
                .fillEmail(user.email())
                .fillPassword(user.password())
                .fillConfirmPassword(user.password())
                .clickRegister()
                .shouldBeRegistered(user.email());
    }
}
