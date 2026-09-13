package com.booking.models;

import com.booking.models.BookingDates;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Booking (

    int roomid,
    String firstname,
    String lastname,
    boolean depositpaid,
    BookingDates bookingdates,
    String email,
    String phone
){
}
