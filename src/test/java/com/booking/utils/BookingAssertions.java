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

    public static void assertRoomIdMatches(TestContext context, Response response, String pathPrefix) {
        Assertions.assertEquals(
                Integer.parseInt(context.getSessionContext("roomid")),
                response.jsonPath().getInt(pathPrefix + "roomid")
        );
    }

    public static void assertRoomIdMatches(TestContext context, Response response) {
        Assertions.assertEquals(
                Integer.parseInt(context.getSessionContext("roomid")),
                response.jsonPath().getInt("roomid")
        );
    }

    public static void assertBookingDetailsMatch(TestContext context, Response response) {
        assertBookingDetailsMatch(context, response, "");
    }

    public static void assertBookingDetailsMatch(TestContext context, Response response, String pathPrefix) {
        Assertions.assertEquals(context.getSessionContext("firstname"), response.jsonPath().getString(pathPrefix + "firstname"));
        Assertions.assertEquals(context.getSessionContext("lastname"), response.jsonPath().getString(pathPrefix + "lastname"));

        String deposit = context.getSessionContext("depositpaid");
        assertDepositPaidMatches(context, response, pathPrefix);

        Assertions.assertEquals(context.getSessionContext("checkin"), response.jsonPath().getString(pathPrefix + "bookingdates.checkin"));
        Assertions.assertEquals(context.getSessionContext("checkout"), response.jsonPath().getString(pathPrefix + "bookingdates.checkout"));
    }

    private static void assertDepositPaidMatches(TestContext context, Response response, String pathPrefix) {
        String deposit = context.getSessionContext("depositpaid");
        if (deposit != null) {
            Assertions.assertEquals(Boolean.parseBoolean(deposit), response.jsonPath().getBoolean(pathPrefix + "depositpaid"));
        }
    }
}
