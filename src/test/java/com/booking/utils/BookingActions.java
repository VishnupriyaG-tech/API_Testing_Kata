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
            logRequestBody(requestBody);
            Response response = given()
                    .spec(context.requestSpec)
                    .body(requestBody)
                    .when()
                    .post(ApiResource.BOOKING.getResource());

            Integer bookingId = response.jsonPath().get("bookingid");
            if (bookingId != null) {
                context.setSessionContext("bookingid", String.valueOf(bookingId));
            }
            return captureResponse(context,response);
        }

        public static Response getBooking(TestContext context, boolean withAuth) {

            Response response = authenticatedRequest(context, withAuth).when()
                    .get(bookingPath(context));
            return captureResponse(context,response);
        }

        public static Response updateBooking(TestContext context, DataTable dataTable, boolean withAuth) {
            Map<String, String> details = dataTable.asMap(String.class, String.class);
            details.forEach(context::setSessionContext);

            int roomId = details.containsKey("roomid")
                    ? Integer.parseInt(details.get("roomid"))
                    : Integer.parseInt(context.getSessionContext("roomid"));

            Booking requestBody = BookingFactory.fromMap(details, roomId);
            logRequestBody(requestBody);

            Response response = authenticatedRequest(context,withAuth)
                    .body(requestBody)
                    .when()
                    .put(bookingPath(context));

            return captureResponse(context,response);
        }

        private static String bookingPath(TestContext context){
            String bookingId = context.getSessionContext("bookingid");
            return ApiResource.BOOKING.getResource() + "/" + bookingId;
        }

        private static RequestSpecification authenticatedRequest(TestContext context, boolean withAuth){
            RequestSpecification request = given().spec(context.requestSpec);

            if (withAuth) {
                String token = context.getSessionContext("token");
                request = request.cookie("token", token);
            }
            return request;
        }

        private static Response captureResponse(TestContext context, Response response){
            log.info("Response Body: " + response.getBody().asPrettyString());
            context.setResponse(response);
            return response;
        }

        private static void logRequestBody(Object requestBody) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                log.info("Request Body: {}", mapper.writeValueAsString(requestBody));
            } catch (Exception e) {
                log.error("Failed to serialize request body", e);
            }
        }
    }