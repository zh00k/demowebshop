package com.github.zh00k.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage extends BasePage<CartPage> {

    private final ElementsCollection productNames = $$("table.cart tr.cart-item-row a.product-name");

    public CartPage() {
        super("/cart", TITLE_PREFIX + "Shopping Cart");
    }

    @Step("Проверить, что в корзине товар «{name}»: {quantity} шт. на сумму {subtotal}")
    public CartPage shouldContainProduct(String name, int quantity, BigDecimal subtotal) {
        SelenideElement row = productNames.findBy(exactText(name)).closest("tr");

        row.$("input.qty-input").shouldHave(exactValue(String.valueOf(quantity)));
        row.$("span.product-subtotal").shouldHave(exactText(subtotal.toPlainString()));
        return this;
    }
}