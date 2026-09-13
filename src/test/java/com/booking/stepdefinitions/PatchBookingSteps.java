package com.booking.stepdefinitions;

import com.booking.utils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;

public class PatchBookingSteps {

    private final TestContext context;

    public PatchBookingSteps(TestContext context) {
        this.context = context;
    }

    @When("I partially update the booking with the following details:")
    public void i_partially_update_the_booking_with_the_following_details(DataTable dataTable) {
        BookingActions.patchBooking(context, dataTable, true);
    }

    @When("I attempt to partially update the booking without an authentication token with the following details:")
    public void i_attempt_to_partially_update_the_booking_without_an_authentication_token(DataTable dataTable) {
        BookingActions.patchBooking(context, dataTable, false);
    }

    @Then("the booking should be partially updated successfully")
    public void the_booking_should_be_partially_updated_successfully() {
        BookingAssertions.assertStatusCode(context.getResponse(), 200);
    }

    @Then("the partially updated booking adheres to the expected response structure")
    public void the_partially_updated_booking_adheres_to_the_expected_response_structure() {
        context.getResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }
}