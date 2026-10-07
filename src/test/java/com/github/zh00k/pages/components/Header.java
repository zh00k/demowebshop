package com.github.zh00k.pages.components;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.pages.CartPage;
import com.github.zh00k.pages.LoginPage;
import com.github.zh00k.pages.RegistrationPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Header {
    private final SelenideElement root         = $("div.header");
    private final SelenideElement registerLink = root.$("a.ico-register");
    private final SelenideElement loginLink    = root.$("a.ico-login");
    private final SelenideElement logOutLink   = root.$("a.ico-logout");
    private final SelenideElement userEmail    = root.$("a.account");
    private final SelenideElement cartLink     = root.$("a.ico-cart");
    private final SelenideElement cartQty      = cartLink.$("span.cart-qty");

    @Step("Нажать ссылку «Register» в шапке")
    public RegistrationPage clickRegister() {
        registerLink.click();
        return new RegistrationPage();
    }

    @Step("Нажать ссылку «Log in» в шапке")
    public LoginPage clickLogin() {
        loginLink.click();
        return new LoginPage();
    }

    @Step("Нажать ссылку «Log out» в шапке")
    public Header clickLogOut() {
        logOutLink.click();
        return this;
    }

    @Step("Открыть корзину из шапки")
    public CartPage clickCart() {
        cartLink.click();
        return new CartPage();
    }

    @Step("Проверить, что пользователь авторизован как {email}")
    public Header shouldBeLoggedInAs(String email) {
        userEmail.shouldBe(exactText(email));
        return this;
    }

    @Step("Проверить, что пользователь не авторизован")
    public Header shouldBeLoggedOut() {
        loginLink.shouldBe(visible);
        userEmail.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что в корзине {quantity} шт.")
    public Header cartShouldHaveQuantity(int quantity) {
        cartQty.shouldHave(exactText("(" + quantity + ")"));
        return this;
    }
}
