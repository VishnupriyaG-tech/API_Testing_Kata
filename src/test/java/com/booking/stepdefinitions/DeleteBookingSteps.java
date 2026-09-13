package com.booking.stepdefinitions;

import com.booking.utils.BookingActions;
import com.booking.utils.BookingAssertions;
import com.booking.utils.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteBookingSteps {

    private final TestContext context;

    public DeleteBookingSteps(TestContext context) {
        this.context = context;
    }

    @When("I delete the booking")
    public void i_delete_the_booking() {
        BookingActions.deleteBooking(context, true);
    }

    @When("I attempt to delete the booking without an authentication token")
    public void i_attempt_to_delete_the_booking_without_an_authentication_token() {
        BookingActions.deleteBooking(context, false);
    }

    @Then("the booking should be deleted successfully")
    public void the_booking_should_be_deleted_successfully() {
        BookingAssertions.assertStatusCode(context.getResponse(), 201);
    }

    @Then("the booking should no longer be retrievable")
    public void the_booking_should_no_longer_be_retrievable() {
        BookingActions.getBooking(context, true);
        BookingAssertions.assertStatusCode(context.getResponse(), 404);
    }
}