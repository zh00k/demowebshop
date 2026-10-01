package com.github.zh00k.pages.components;

import com.codeborne.selenide.SelenideElement;
import com.github.zh00k.pages.SubcategoryPage;

import java.util.Objects;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class HeaderMenu {
    private final SelenideElement root = $("div.header-menu");

    public SubcategoryPage openSubcategory(String category, String subcategory) {
        SelenideElement categoryMenuElement = root.$$("ul.top-menu > li > a").findBy(exactText(category)).parent();
        SelenideElement subcategoryMenuElement = categoryMenuElement.$$("ul.sublist > li > a").findBy(exactText(subcategory));

        categoryMenuElement.hover();

        String href = Objects.requireNonNull(subcategoryMenuElement.attr("href"),
                "У пункта меню '" + subcategory + "' нет href");
        String path = href.substring(href.lastIndexOf('/') + 1);

        subcategoryMenuElement.click();

        return new SubcategoryPage(path, subcategory);
    }
}