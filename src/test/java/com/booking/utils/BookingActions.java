package com.booking.utils;

import com.booking.models.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

    public class BookingActions {
        private static final Logger log = LoggerFactory.getLogger(BookingActions.class);

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
                log.info("Request Body: " + json);
            } catch (Exception e) {
                log.error("Failed to serialize request body: " + e.getMessage());
            }

            Response response = given()
                    .spec(context.requestSpec)
                    .body(requestBody)
                    .when()
                    .post(ApiResource.BOOKING.getResource());

            log.info("Response Body: " + response.getBody().asPrettyString());
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

            RequestSpecification request = given().spec(context.requestSpec);

            if (withAuth) {
                String token = context.getSessionContext("token");
                request = request.cookie("token", token);
            }

            Response response = request
                    .when()
                    .get(path);

            log.info("Response Body: " + response.getBody().asPrettyString());

            context.setResponse(response);
            return response;
        }

        public static Response updateBooking(TestContext context, DataTable dataTable, boolean withAuth) {
            Map<String, String> details = dataTable.asMap(String.class, String.class);
            details.forEach(context::setSessionContext);

            int roomId = details.containsKey("roomid")
                    ? Integer.parseInt(details.get("roomid"))
                    : Integer.parseInt(context.getSessionContext("roomid"));
            Booking requestBody = BookingFactory.fromMap(details, roomId);

            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(requestBody);
                log.info("Request Body: " + json);
            } catch (Exception e) {
                log.error("Failed to serialize request body: " + e.getMessage());
            }

            String bookingId = context.getSessionContext("bookingid");
            String path = ApiResource.BOOKING.getResource() + "/" + bookingId;

            RequestSpecification request = given()
                    .spec(context.requestSpec)
                    .body(requestBody);

            if (withAuth) {
                String token = context.getSessionContext("token");
                request = request.cookie("token", token);
            }

            Response response = request
                    .when()
                    .put(path);

            log.info("Response Body: " + response.getBody().asPrettyString());

            context.setResponse(response);
            return response;
        }
    }