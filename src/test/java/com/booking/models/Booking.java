package com.booking.models;

import com.booking.models.BookingDates;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String email;
    private String phone;
}
