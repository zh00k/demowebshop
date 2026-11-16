package com.github.zh00k.booking.util;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomAllureListener {

    private static final AllureRestAssured filter = new AllureRestAssured();

    public static AllureRestAssured withCustomTemplates() {
        filter.setRequestTemplate("request.ftl");
        filter.setResponseTemplate("response.ftl");
        return filter;
    }
}
