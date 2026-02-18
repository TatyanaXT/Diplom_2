package org.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class UserActions {

    public static final String BASE_URI = "https://stellarburgers.education-services.ru";

    public String generateUserEmail (){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        String partLogin = formater.format(date);

        return "email_" + partLogin + "@ya.ru";
    }

    public String getAccessToken(Response response) {
        if (response.statusCode() == 200) {
            String token = response.path("accessToken");
            return token.split(" ")[1];
        }
        return null;
    }


    @Step("Send POST request to /api/auth/register")
    public Response createUser(User data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/auth/register");

    }

    @Step("Send POST request to /api/auth/login")
    public Response authorizationUser(User data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/auth/login");
    }

    @Step("Send POST request to /api/auth/register")
    public Response registerUser(User data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/auth/register");
    }

    @Step("Send POST request to /api/auth/token")
    public Response updateTokenUser(User data) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(data)
                .when()
                .post("/api/auth/token");
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response updateUser(User data, String token) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .auth().oauth2(token)
                .and()
                .body(data)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Send DELETE request to /api/auth/user")
    public void deleteUser(String token) {
        given()
            .contentType(ContentType.JSON)
            .baseUri(BASE_URI)
            .auth().oauth2(token)
            .when()
            .delete("/api/auth/user");
    }

}
