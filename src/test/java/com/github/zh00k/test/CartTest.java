package com.github.zh00k.test;

import com.github.zh00k.pages.ProductPage;
import com.github.zh00k.pages.WelcomePage;
import com.github.zh00k.steps.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class CartTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        new UserSteps().registerUser();
    }

    @Test
    void addItemToCart() {
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