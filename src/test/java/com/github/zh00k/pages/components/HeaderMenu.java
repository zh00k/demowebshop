package com.github.zh00k.pages.components;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.pages.SubcategoryPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class HeaderMenu {
    private final SelenideElement root = $("div.header-menu");

    @Step("Открыть подкатегорию «{category} → {subcategory}»")
    public SubcategoryPage openSubcategory(String category, String subcategory, String path) {
        SelenideElement categoryMenuElement = root.$$("ul.top-menu > li > a").findBy(exactText(category)).parent();

        categoryMenuElement.hover();
        categoryMenuElement.$$("ul.sublist > li > a").findBy(exactText(subcategory)).click();

        return new SubcategoryPage(path, subcategory);
    }
}
