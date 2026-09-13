package com.booking.stepdefinitions;

import com.booking.utils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class UpdateBookingSteps {

    private final TestContext context;

    public UpdateBookingSteps(TestContext context) {
        this.context = context;
    }

    @When("I update the booking with the following details:")
    public void i_update_the_booking_with_the_following_details(DataTable dataTable) {
        BookingActions.updateBooking(context, dataTable, true);
    }

    @When("I attempt to update the booking without an authentication token with the following details:")
    public void i_attempt_to_update_the_booking_without_an_authentication_token(DataTable dataTable) {
        BookingActions.updateBooking(context, dataTable, false);
    }

    @Then("the booking should be updated successfully")
    public void the_booking_should_be_updated_successfully() {
        BookingAssertions.assertStatusCode(context.getResponse(), 200);
    }

    @Then("the updated booking details should reflect the submitted changes")
    public void the_updated_booking_details_should_reflect_the_submitted_changes() {
        BookingAssertions.assertBookingDetailsMatch(context, context.getResponse());
    }

    @Then("the updated booking adheres to the expected response structure")
    public void the_updated_booking_adheres_to_the_expected_response_structure() {
        context.getResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }
}
