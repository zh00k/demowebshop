package com.github.zh00k.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class JenkinsTest {
    @Test
    @Tags({@Tag("UI"), @Tag("Positive")})
    @DisplayName("UI Positive")
    void jenkinsTest1() {
        System.out.println("UI Positive Test");
    }

    @Test
    @Tags({@Tag("UI"), @Tag("Negative")})
    @DisplayName("UI Negative")
    void jenkinsTest2() {
        System.out.println("UI Negative Test");
    }

    @Test
    @Tags({@Tag("API"), @Tag("Positive")})
    @DisplayName("API Positive")
    void jenkinsTest3() {
        System.out.println("API Positive Test");
    }

    @Test
    @Tags({@Tag("API"), @Tag("Negative")})
    @DisplayName("API Negative")
    void jenkinsTest4() {
        System.out.println("API Negative Test");
    }
}
