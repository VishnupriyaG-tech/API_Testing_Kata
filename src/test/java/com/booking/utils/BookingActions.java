package com.booking.utils;

import com.booking.models.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

    public class BookingActions {

        private BookingActions() {
            // utility class, no instances
        }

        public static Response createBooking(TestContext context, DataTable dataTable) {
            Map<String, String> details = dataTable.asMap(String.class, String.class);
            details.forEach(context::setSessionContext);

            int roomId = Integer.parseInt(context.getSessionContext("roomid"));
            Booking requestBody = BookingFactory.fromMap(details, roomId);

            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(requestBody);
                System.out.println("Request Body: " + json);
            } catch (Exception e) {
                System.out.println("Failed to serialize request body: " + e.getMessage());
            }

            Response response = given()
                    .spec(context.postPutRequestSpec)
                    .body(requestBody)
                    .when()
                    .post(ApiResource.BOOKING.getResource());

            System.out.println("Response Body: " + response.getBody().asPrettyString());
            Integer bookingId = response.jsonPath().get("bookingid");
            if (bookingId != null) {
                context.setSessionContext("bookingid", String.valueOf(bookingId));
            }
            context.setResponse(response);
            return response;
        }

        public static Response getBooking(TestContext context, boolean withAuth) {
            String bookingId = context.getSessionContext("bookingid");
            String path = ApiResource.BOOKING.getResource() + "/" + bookingId;

            RequestSpecification request = given().spec(context.postPutRequestSpec);

            if (withAuth) {
                String token = context.getSessionContext("token");
                request = request.cookie("token", token);
            }

            Response response = request
                    .when()
                    .get(path);

            System.out.println("Response Body: " + response.getBody().asPrettyString());

            context.setResponse(response);
            return response;
        }
    }
