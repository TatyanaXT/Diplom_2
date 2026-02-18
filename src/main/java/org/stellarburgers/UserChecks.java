package org.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserChecks {

    @Step("Проверка ответа при успешном создании пользователя")
    public void checkCreationUserSuccess(Response response){

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Step("Проверка ответа при создании пользователя, который уже зарегистрирован")
    public void checkCreationUserExist(Response response){

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);

    }

    @Step("Проверка ответа при создании пользователя без передачи одного из обязательных полей")
    public void checkCreationUserWithoutField(Response response){

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);

    }

    @Step("Проверка ответа при успешном логине пользователя")
    public void checkLoginUserSuccess(Response response){

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Step("Проверка ответа при логине с неверными данными")
    public void checkLoginUserIncorrect(Response response){

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }

    @Step("Проверка ответа при успешном обновлении пользователя")
    public void checkUpdateUserSuccess(Response response){

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Step("Проверка ответа при обновлении неавторизованным пользователем")
    public void checkUpdateUserUnauthorised(Response response){

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }
}
