package com.booking.stepdefinitions;

import com.booking.utils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingSteps {

    private final TestContext context;
    private Response response;

    public BookingSteps(TestContext context) {
        this.context = context;
    }

    @Given("the Booking service is up and running")
    public void the_booking_service_is_up_and_running() {
        given()
                .spec(context.postPutRequestSpec)
                .when()
                .get(ApiResource.HEALTH.getResource())
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }

    @Given("a guest wants to reserve a room")
    public void a_guest_wants_to_reserve_a_room() {
        int roomId = BookingFactory.newRoomId();
        context.setSessionContext("roomid", String.valueOf(roomId));
    }

    @When("I create a booking with the following details:")
    public void i_create_a_booking_with_the_following_details(DataTable dataTable) {
        response = BookingActions.createBooking(context, dataTable);
    }

    @When("I create a booking with the incorrect requested details:")
    public void i_create_a_booking_with_the_incorrect_requested_details(DataTable dataTable) {
        response = BookingActions.createBooking(context, dataTable);
    }

    @Then("the booking should be created successfully")
    public void the_booking_should_be_created_successfully() {
        BookingAssertions.assertStatusCode(response, 201);
    }

    @Then("the response should contain a valid booking id")
    public void the_response_should_contain_a_valid_booking_id() {
        Assertions.assertNotNull(response.jsonPath().get("bookingid"), "bookingid was missing from the response");
    }

    @Then("the booking confirmation should reflect the submitted guest and stay details")
    public void the_booking_confirmation_should_reflect_the_submitted_details() {
        BookingAssertions.assertRoomIdMatches(context, response);
        BookingAssertions.assertBookingDetailsMatch(context, response);
        // Note: email and phone are submitted in the request but are not
        // returned in the response body by this API, so they are not
        // asserted here.
    }

    @Then("the booking confirmation adheres to the expected response structure")
    public void the_booking_confirmation_adheres_to_the_expected_response_structure() {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }

    @Then("the system rejects the booking request due to validation errors")
    public void the_system_rejects_the_booking_request_due_to_validation_errors() {
        BookingAssertions.assertStatusCode(response, 400);
    }

    @Then("the validation error message indicates {string}")
    public void the_validation_error_message_indicates(String expectedMessage) {
        List<String> errors = response.jsonPath().getList("errors", String.class);
        Assertions.assertTrue(
                errors != null && errors.contains(expectedMessage),
                "Expected error message \"" + expectedMessage + "\" not found. Actual errors: " + errors
        );
    }
}