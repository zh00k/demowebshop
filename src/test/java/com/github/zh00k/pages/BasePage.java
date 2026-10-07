package com.github.zh00k.pages;

import com.codeborne.selenide.Selenide;
import com.github.zh00k.pages.components.Header;
import com.github.zh00k.pages.components.HeaderMenu;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;


public abstract class BasePage<T extends BasePage<T>> {
    protected static final String TITLE_PREFIX = "Demo Web Shop. ";

    private final String pageUrl;
    private final String pageTitle;

    protected BasePage(String pageUrl, String pageTitle) {
        this.pageUrl = pageUrl;
        this.pageTitle = pageTitle;
    }

    public Header header() {
        return new Header();
    }

    public HeaderMenu headerMenu() {
        return new HeaderMenu();
    }

    @Step("Открыть страницу")
    public T open() {
        if (pageUrl == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " не открывается по прямому URL");
        }

        Selenide.open(pageUrl);
        return self();
    }

    @Step("Проверить, что страница открыта")
    public T shouldBeOpened() {
        webdriver().shouldHave(urlContaining(pageUrl));
        return self();
    }

    @Step("Проверить заголовок страницы")
    public T shouldHaveCorrectTitle() {
        webdriver().shouldHave(title(pageTitle));
        return self();
    }

    @Step("Переключиться на окно {index}")
    public <P> P switchToWindow(int index, Class<P> pageClass) {
        switchTo().window(index);
        return page(pageClass);
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}