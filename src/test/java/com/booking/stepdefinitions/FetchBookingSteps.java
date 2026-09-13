package com.booking.stepdefinitions;

import com.booking.utils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;

public class FetchBookingSteps {

    private final TestContext context;

    public FetchBookingSteps(TestContext context) {
        this.context = context;
    }

    @Given("a booking has been created with the following details:")
    public void a_booking_has_been_created_with_the_following_details(DataTable dataTable) {
        BookingActions.createBooking(context, dataTable);
    }

    @Given("the user has logged in with valid credentials")
    public void the_user_has_logged_in_with_valid_credentials() {
        AuthActions.login(context, ConfigReader.getAuthUsername(), ConfigReader.getAuthPassword());
    }

    @When("I retrieve the booking using its booking id")
    public void i_retrieve_the_booking_using_its_booking_id() {
        BookingActions.getBooking(context, true);
    }

    @Then("the booking should be retrieved successfully")
    public void the_booking_should_be_retrieved_successfully() {
        BookingAssertions.assertStatusCode(context.getResponse(), 200);
    }

    @Then("the retrieved booking details should match the originally submitted details")
    public void the_retrieved_booking_details_should_match_the_originally_submitted_details() {
        BookingAssertions.assertBookingDetailsMatch(context, context.getResponse());
    }

    @Then("the retrieved booking adheres to the expected response structure")
    public void the_retrieved_booking_adheres_to_the_expected_response_structure() {
        context.getResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }

    @When("I attempt to retrieve the booking without an authentication token")
    public void i_attempt_to_retrieve_the_booking_without_an_authentication_token() {
        BookingActions.getBooking(context, false);
    }

    @Then("the request should be rejected as unauthorized")
    public void the_request_should_be_rejected_as_unauthorized() {
        BookingAssertions.assertStatusCode(context.getResponse(), 401);
    }
}