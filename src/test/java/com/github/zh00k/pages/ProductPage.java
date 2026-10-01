package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class ProductPage extends BasePage<ProductPage> {

    private final String name;

    private final SelenideElement productName                        = $("div.product-name > h1");
    private final SelenideElement price                              = $("span[itemprop='price']");
    private final SelenideElement addToCartButton                    = $("input.add-to-cart-button");
    private final SelenideElement succesfullyAddedToCartNotification = $("div#bar-notification.success");

    public ProductPage(String name) {
        super(null, name);
        this.name = name;
    }

    @Override
    public ProductPage shouldBeOpened() {
        productName.shouldHave(exactText(name));
        return this;
    }


}
