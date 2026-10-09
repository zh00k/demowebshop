package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterResultPage extends BasePage<RegisterResultPage> {
    private final SelenideElement resultText = $("div.result");

    public RegisterResultPage() {
        super("/registerresult/", TITLE_PREFIX + "Register");
    }

    @Step("Проверить успешную регистрацию пользователя {email}")
    public RegisterResultPage shouldBeRegistered(String email) {
        resultText.shouldHave(text("Your registration completed"));
        header().shouldBeLoggedInAs(email);
        return this;
    }
}
