package com.github.zh00k.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage extends BasePage<CartPage> {

    private final ElementsCollection productNames = $$("table.cart tr.cart-item-row a.product-name");

    public CartPage() {
        super("/cart", TITLE_PREFIX + "Shopping Cart");
    }

    public String getQuantity(String name) {
        return row(name).$("input.qty-input").getValue();
    }

    public String getUnitPrice(String name) {
        return row(name).$("span.product-unit-price").getText();
    }

    public String getSubtotal(String name) {
        return row(name).$("span.product-subtotal").getText();
    }

    private SelenideElement row(String name) {
        return productNames.findBy(exactText(name)).closest("tr");
    }
}
