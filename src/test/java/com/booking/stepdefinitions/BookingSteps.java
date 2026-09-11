package com.booking.stepdefinitions;

import com.booking.models.Booking;
import com.booking.utils.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingSteps {

    private final TestContext context;
    private Booking requestBody;
    private Response response;

    public BookingSteps(TestContext context) {
        this.context = context;
    }

    @Given("the Booking service is up and running")
    public void the_booking_service_is_up_and_running() {
        given()
                .spec(context.postPutRequestSpec)
                .when()
                .get(Resources.HEALTH.getResource())
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
        buildAndSendBooking(dataTable);
    }

    @When("I create a booking with the incorrect requested details:")
    public void i_create_a_booking_with_the_incorrect_requested_details(DataTable dataTable) {
        buildAndSendBooking(dataTable);
    }

    private void buildAndSendBooking(DataTable dataTable) {
        Map<String, String> details = dataTable.asMap(String.class, String.class);
        details.forEach(context::setSessionContext);

        int roomId = Integer.parseInt(context.getSessionContext("roomid"));
        requestBody = BookingFactory.fromMap(details, roomId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(requestBody);
            System.out.println("Request Body: " + json);
        } catch (Exception e) {
            System.out.println("Failed to serialize request body: " + e.getMessage());
        }

        response = given()
                .spec(context.postPutRequestSpec)
                .body(requestBody)
                .when()
                .post(Resources.BOOKING.getResource());

        System.out.println("Response Body: " + response.getBody().asPrettyString());

        context.setResponse(response);
    }

    @Then("the booking should be created successfully")
    public void the_booking_should_be_created_successfully() {
        Assertions.assertEquals(201, response.getStatusCode(), "Unexpected status code");
    }

    @Then("the response should contain a valid booking id")
    public void the_response_should_contain_a_valid_booking_id() {
        Assertions.assertNotNull(response.jsonPath().get("bookingid"), "bookingid was missing from the response");
    }

    @Then("the booking confirmation should reflect the submitted guest and stay details")
    public void the_booking_confirmation_should_reflect_the_submitted_details() {
        Assertions.assertEquals(Integer.parseInt(context.getSessionContext("roomid")), response.jsonPath().getInt("roomid"));
        Assertions.assertEquals(context.getSessionContext("firstname"), response.jsonPath().getString("firstname"));
        Assertions.assertEquals(context.getSessionContext("lastname"), response.jsonPath().getString("lastname"));

        String deposit = context.getSessionContext("depositpaid");
        if (deposit != null) {
            Assertions.assertEquals(Boolean.parseBoolean(deposit), response.jsonPath().getBoolean("depositpaid"));
        }

        Assertions.assertEquals(context.getSessionContext("checkin"), response.jsonPath().getString("bookingdates.checkin"));
        Assertions.assertEquals(context.getSessionContext("checkout"), response.jsonPath().getString("bookingdates.checkout"));
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
        Assertions.assertEquals(400, response.getStatusCode(), "Unexpected status code");
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

