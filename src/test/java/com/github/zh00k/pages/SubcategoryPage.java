package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.data.Subcategory;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class SubcategoryPage extends BasePage<SubcategoryPage> {

    private final SelenideElement productGrid = $("div.product-grid");

    public SubcategoryPage(Subcategory subcategory) {
        super("/" + subcategory.path(), TITLE_PREFIX + subcategory.title());
    }

    public ProductPage openProduct(String name) {
        productGrid.$$("h2.product-title a").findBy(exactText(name)).click();
        return new ProductPage(name);
    }
}
