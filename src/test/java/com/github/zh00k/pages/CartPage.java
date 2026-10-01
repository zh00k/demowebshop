package com.github.zh00k.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage extends BasePage<CartPage> {

    private final ElementsCollection productNames = $$("table.cart tr.cart-item-row a.product-name");

    public CartPage() {
        super("/cart", TITLE_PREFIX + "Shopping Cart");
    }

    public CartPage shouldContainProduct(String name, int quantity, float price) {
        SelenideElement row = productNames.findBy(exactText(name)).closest("tr");

        row.$("input.qty-input").shouldHave(exactValue(String.valueOf(quantity)));
        row.$("span.product-subtotal").shouldHave(exactValue(String.valueOf(price)));
        return this;
    }
}