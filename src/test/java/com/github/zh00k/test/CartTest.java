package com.github.zh00k.test;

import com.github.zh00k.pages.ProductPage;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.zh00k.config.Config.KAITEN_ALLURE_URL;
import static com.github.zh00k.config.Config.KAITEN_ARCHITECTURE_URL;


@Epic("Demo Web Shop")
@Feature("Корзина")
public class CartTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        new UserSteps().registerUser();
    }

    @Test
    @DisplayName("Добавление нескольких единиц товара в корзину авторизованным пользователем")
    @Tag("positive")
    @Owner("zh00k")
    @Link(name = "Архитектура тестов", url = KAITEN_ARCHITECTURE_URL)
    @Link(name = "Allure", url = KAITEN_ALLURE_URL)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Добавление товара в корзину")
    void addItemsToCart() {
        String productName = "Build your own cheap computer";

        int itemQuantity = 2;

        ProductPage productPage = new WelcomePage()
                .open()
                .headerMenu()
                .openSubcategory("Computers", "Desktops")
                .openProduct(productName)
                .shouldBeOpened()
                .setItemQuantity(itemQuantity)
                .selectProcessor(0);

        BigDecimal itemPrice        = productPage.getItemPrice();
        BigDecimal expectedSubtotal = itemPrice.multiply(new BigDecimal(itemQuantity));

        productPage
                .clickAddToCartButton()
                .shouldShowAddedToCartNotification()
                .header()
                .cartShouldHaveQuantity(itemQuantity)
                .clickCart()
                .shouldBeOpened()
                .shouldHaveCorrectTitle()
                .shouldContainProduct(productName, itemQuantity, expectedSubtotal);
    }
}