package com.github.zh00k.test;

import com.github.zh00k.data.Subcategory;
import com.github.zh00k.pages.SubcategoryPage;
import com.github.zh00k.steps.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CartTest extends BaseTest {

    private SubcategoryPage subcategoryPage = new SubcategoryPage(Subcategory.DESKTOPS);

    @BeforeEach
    void beforeEach() {
        new UserSteps().registerUser();
    }

    @Test
    void test() {
        subcategoryPage
                .open()
                .openProduct("Build your own cheap computer")
                .shouldBeOpened();
    }

}
