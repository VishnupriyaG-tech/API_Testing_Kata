package com.booking.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds state shared across step definition classes for a single scenario.
 * A new instance is created per scenario by PicoContainer and injected via
 * constructor into any step definition class that declares it as a parameter.
 */
public class TestContext {

    private final Map<String, String> sessionContext = new HashMap<>();
    private Response response;

    public final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(ConfigReader.getBaseUrl())
            .setContentType("application/json")
            .build();

    public void setSessionContext(String key, String value) {
        sessionContext.put(key, value);
    }

    public String getSessionContext(String key) {
        return sessionContext.get(key);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
