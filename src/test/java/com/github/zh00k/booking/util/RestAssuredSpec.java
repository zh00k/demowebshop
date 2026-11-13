package com.github.zh00k.booking.util;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class RestAssuredSpec {
    public static RequestSpecification reqSpec = with()
        .log().uri()
        .log().headers()
        .log().body();

    public static ResponseSpecification resSpec = new ResponseSpecBuilder()
        .log(LogDetail.BODY)
        .build();

    public static void setupRestAssured() {
        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = resSpec;
    }
}
