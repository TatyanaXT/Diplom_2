package org.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderActions {

    public static final String BASE_URI = "https://stellarburgers.education-services.ru";

    @Step("Send POST request to /api/orders")
    public Response createOrder(String token, String json) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .auth().oauth2(token)
                .and()
                .body(json)
                .when()
                .post("/api/orders");

    }

    @Step("Send GET request to /api/orders")
    public Response getOrder(String token) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .auth().oauth2(token)
                .when()
                .get("/api/orders");

    }
}
