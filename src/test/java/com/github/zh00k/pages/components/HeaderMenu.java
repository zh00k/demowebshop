package com.github.zh00k.pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class HeaderMenu {
    private final SelenideElement root              = $("div.header-menu");
    private final SelenideElement computersCategory = root.$("a[href='/computers']");
    private final SelenideElement desktopsSubcategory = computersCategory.$("a[href='/desktops']");



}
