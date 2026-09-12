package com.booking.utils;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthActions {

    private AuthActions() {
        // utility class, no instances
    }

    public static String login(TestContext context, String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        Response response = given()
                .spec(context.postPutRequestSpec)
                .body(credentials)
                .when()
                .post(ApiResource.AUTH.getResource());

        String token = response.jsonPath().getString("token");
        context.setSessionContext("token", token);
        return token;
    }
}
