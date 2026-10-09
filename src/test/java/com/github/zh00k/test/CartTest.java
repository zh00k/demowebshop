package com.github.zh00k.test;

import com.github.zh00k.pages.CartPage;
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

import static com.github.zh00k.config.Config.KAITEN_ALLURE_URL;
import static com.github.zh00k.config.Config.KAITEN_ARCHITECTURE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

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
        String processor   = "Fast";

        int itemQuantity = 2;

        ProductPage productPage = new WelcomePage()
                .open()
                .headerMenu()
                .openSubcategory("Computers", "Desktops", "desktops")
                .openProduct(productName)
                .shouldBeOpened();

        BigDecimal expectedUnitPrice = productPage.getItemPrice().add(processorSurcharge(processor));
        BigDecimal expectedSubtotal  = expectedUnitPrice.multiply(new BigDecimal(itemQuantity));

        CartPage cartPage = productPage
                .setItemQuantity(itemQuantity)
                .selectProcessor(processor)
                .clickAddToCartButton()
                .shouldShowAddedToCartNotification()
                .header()
                .cartShouldHaveQuantity(itemQuantity)
                .clickCart()
                .shouldBeOpened()
                .shouldHaveCorrectTitle();

        assertAll(
                () -> assertEquals(String.valueOf(itemQuantity), cartPage.getQuantity(productName)),
                () -> assertEquals(expectedUnitPrice.toPlainString(), cartPage.getUnitPrice(productName)),
                () -> assertEquals(expectedSubtotal.toPlainString(), cartPage.getSubtotal(productName))
        );
    }

    private BigDecimal processorSurcharge(String processor) {
        return switch (processor) {
            case "Slow" -> BigDecimal.ZERO;
            case "Medium" -> new BigDecimal("15.00");
            case "Fast" -> new BigDecimal("100.00");
            default -> throw new IllegalArgumentException("Unknown processor: " + processor);
        };
    }
}
