package com.github.zh00k.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductPage extends BasePage<ProductPage> {

    private final String name;

    private final ElementsCollection productAttributes = $$("div.attributes dt");

    private final SelenideElement productName                         = $("div.product-name > h1");
    private final SelenideElement price                               = $("span[itemprop='price']");
    private final SelenideElement addToCartButton                     = $("input.add-to-cart-button");
    private final SelenideElement successfullyAddedToCartNotification = $("div#bar-notification.success");
    private final SelenideElement qtyInput                            = $("input.qty-input");
    private final SelenideElement processorBlock                      = productAttributes.findBy(text("Processor")).sibling(0);

    public ProductPage(String name) {
        super(null, name);
        this.name = name;
    }

    @Override
    public ProductPage shouldBeOpened() {
        productName.shouldHave(exactText(name));
        return this;
    }

    public ProductPage shouldShowAddedToCartNotification() {
        successfullyAddedToCartNotification.shouldBe(visible);
        return this;
    }

    public ProductPage clickAddToCartButton() {
        addToCartButton.click();
        return this;
    }

    public ProductPage setItemQuantity(int quantity) {
        qtyInput.setValue(String.valueOf(quantity));
        return this;
    }

    public BigDecimal getItemPrice() {
        return new BigDecimal(price.getText());
    }

    /**
     * index 0 = slow, 1 = medium, 2 = fast
     */
    public ProductPage selectProcessor(int index) {
        processorBlock.$$("input[type=radio]").get(index).click();
        return this;
    }
}
