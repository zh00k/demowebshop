package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ProductPage extends BasePage<ProductPage> {

    private final String name;

    private final SelenideElement productName                         = $("div.product-name > h1");
    private final SelenideElement price                               = $("span[itemprop='price']");
    private final SelenideElement addToCartButton                     = $("input.add-to-cart-button");
    private final SelenideElement successfullyAddedToCartNotification = $("div#bar-notification.success");
    private final SelenideElement qtyInput                            = $("input.qty-input");
    private final SelenideElement slowProcessorOption                 = $(byText("Slow"));

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

    public ProductPage selectProcessor() {
        slowProcessorOption.click();
        return this;
    }
}
