@API @Booking @Auth
Feature: Partially Update Room Reservation Details
  As a user
  I want to update selected details of an existing booking
  So that I can make small corrections without resubmitting the entire reservation

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully partially update an existing booking with valid authentication
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Hari             |
      | lastname    | Raju             |
      | depositpaid | true             |
      | checkin     | 2026-10-09       |
      | checkout    | 2026-10-10       |
      | email       | hari@gmail.com   |
      | phone       | 987560067800     |
    And the user has logged in with valid credentials
    When I partially update the booking with the following details:
      | firstname   | Priyanka |
      | depositpaid | false    |
    Then the booking should be partially updated successfully
    And the updated booking details should reflect the submitted changes

  @regression @negative @auth
  Scenario: Fail to partially update a booking without providing an authentication token
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Devi             |
      | lastname    | Dharani          |
      | depositpaid | true             |
      | checkin     | 2026-10-08       |
      | checkout    | 2026-10-10       |
      | email       | devi@gmail.com   |
      | phone       | 987542467800     |
    When I attempt to partially update the booking without an authentication token with the following details:
      | firstname   | Uma           |
      | email       | uma@gmail.com |
    Then the request should be rejected as unauthorized
