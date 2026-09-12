package com.booking.utils;

public enum ApiResource {

    BOOKING(ConfigReader.getBookingEndpoint()),
    HEALTH(ConfigReader.getBookingHealthCheck()),
    AUTH(ConfigReader.getAuthEndpoint());

    private final String resource;

    ApiResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
