package com.github.zh00k.test;

import com.github.zh00k.pages.CartPage;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CartTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        new UserSteps().registerUser();
    }

    @Test
    void addItemToCart() {
        String productName = "Build your own cheap computer";

        int itemQuantity = 2;

        new WelcomePage()
                .open()
                .headerMenu()
                .openSubcategory("Computers", "Desktops")
                .openProduct(productName)
                .shouldBeOpened()
                .setQuantity(2)
                .clickAddToCartButton()
                .shouldShowAddedToCartNotification()
                .header()
                .cartShouldHaveQuantity(itemQuantity)
                .clickCart()
                .shouldBeOpened()
                .shouldHaveCorrectTitle()
                .shouldContainProduct(productName, itemQuantity, 1600);
    }
}