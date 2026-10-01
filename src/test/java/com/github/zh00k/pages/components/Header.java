package com.github.zh00k.pages.components;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.pages.CartPage;
import com.github.zh00k.pages.LoginPage;
import com.github.zh00k.pages.RegistrationPage;

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

    public RegistrationPage clickRegister() {
        registerLink.click();
        return new RegistrationPage();
    }

    public LoginPage clickLogin() {
        loginLink.click();
        return new LoginPage();
    }

    public Header clickLogOut() {
        logOutLink.click();
        return this;
    }

    public CartPage clickCart() {
        cartLink.click();
        return new CartPage();
    }

    public Header shouldBeLoggedInAs(String email) {
        userEmail.shouldBe(exactText(email));
        return this;
    }

    public Header shouldBeLoggedOut() {
        loginLink.shouldBe(visible);
        userEmail.shouldNotBe(visible);
        return this;
    }

    public Header cartShouldHaveQuantity(int quantity) {
        cartQty.shouldHave(exactText("(" + quantity + ")"));
        return this;
    }
}
