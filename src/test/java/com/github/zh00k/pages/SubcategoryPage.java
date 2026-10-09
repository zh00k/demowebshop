package com.github.zh00k.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.Objects;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class SubcategoryPage extends BasePage<SubcategoryPage> {

    private final SelenideElement productGrid = $("div.product-grid");

    public SubcategoryPage(String path, String title) {
        super("/" + Objects.requireNonNull(path, "path не должен быть null"), TITLE_PREFIX + title);
    }

    @Step("Открыть товар «{name}»")
    public ProductPage openProduct(String name) {
        productGrid.$$("h2.product-title a").findBy(exactText(name)).click();
        return new ProductPage(name);
    }
}
