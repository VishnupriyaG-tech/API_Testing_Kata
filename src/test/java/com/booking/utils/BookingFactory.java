package com.booking.utils;

import com.booking.models.Booking;
import com.booking.models.BookingDates;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BookingFactory {

    private BookingFactory() {
        // utility class, no instances
    }

    public static int newRoomId() {
        return ThreadLocalRandom.current().nextInt(1000, 2000);
    }

    public static Booking fromMap(Map<String, String> row, int roomId) {
        BookingDates dates = new BookingDates(
                row.get("checkin"),
                row.get("checkout")
        );

        return new Booking(
                roomId,
                row.get("firstname"),
                row.get("lastname"),
                Boolean.parseBoolean(row.get("depositpaid")),
                dates,
                row.get("email"),
                row.get("phone")
        );
    }
}
