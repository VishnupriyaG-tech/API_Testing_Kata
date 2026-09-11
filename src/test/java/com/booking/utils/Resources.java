package com.booking.utils;

import com.booking.utils.ConfigReader;

public enum Resources {

    BOOKING(ConfigReader.getBookingEndpoint()),
    HEALTH(ConfigReader.getBookingHealthCheck());

    private final String resource;

    Resources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
