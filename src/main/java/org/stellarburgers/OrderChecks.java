package org.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderChecks {

    @Step("Проверка ответа при успешном создании заказа")
    public void checkCreationOrderSuccess(Response response) {

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Step("Проверка ответа при создании заказа без ингредиентов")
    public void checkCreationOrderWithoutIngredient(Response response) {

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);

    }

    @Step("Проверка ответа при создании заказа с неверным хешем ингредиентов")
    public void checkCreationOrderWithIncorrectHash(Response response) {

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    @Step("Проверка ответа при успешном получении заказов пользователя")
    public void checkGetOrdersSuccess(Response response) {

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);

    }

    @Step("Проверка ответа при получении заказов неавторизованным пользователем")
    public void checkGetOrdersUnauthorised(Response response) {

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }
}