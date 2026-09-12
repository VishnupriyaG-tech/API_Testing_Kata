package com.booking.utils;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class BookingAssertions {

    private BookingAssertions() {
        // utility class, no instances
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), "Unexpected status code");
    }

    public static void assertRoomIdMatches(TestContext context, Response response) {
        Assertions.assertEquals(
                Integer.parseInt(context.getSessionContext("roomid")),
                response.jsonPath().getInt("roomid")
        );
    }

    public static void assertBookingDetailsMatch(TestContext context, Response response) {
        Assertions.assertEquals(context.getSessionContext("firstname"), response.jsonPath().getString("firstname"));
        Assertions.assertEquals(context.getSessionContext("lastname"), response.jsonPath().getString("lastname"));

        String deposit = context.getSessionContext("depositpaid");
        if (deposit != null) {
            Assertions.assertEquals(Boolean.parseBoolean(deposit), response.jsonPath().getBoolean("depositpaid"));
        }

        Assertions.assertEquals(context.getSessionContext("checkin"), response.jsonPath().getString("bookingdates.checkin"));
        Assertions.assertEquals(context.getSessionContext("checkout"), response.jsonPath().getString("bookingdates.checkout"));
    }
}
