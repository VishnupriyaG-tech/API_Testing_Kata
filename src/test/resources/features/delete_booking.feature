@API @Booking @Auth
Feature: Delete Room Reservation
  As a user
  I want to delete an existing booking
  So that I can cancel a reservation that is no longer needed

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully delete an existing booking with valid authentication
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Aravind              |
      | lastname    | Raja                 |
      | depositpaid | true                 |
      | checkin     | 2026-10-08           |
      | checkout    | 2026-10-09           |
      | email       | aravind@gmail.com    |
      | phone       | 882242467800         |
    And the user has logged in with valid credentials
    When I delete the booking
    Then the booking should be deleted successfully
    And the booking should no longer be retrievable

  @regression @negative @auth
  Scenario: Fail to delete a booking without providing an authentication token
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Divya                |
      | lastname    | Raja                 |
      | depositpaid | true                 |
      | checkin     | 2026-10-13           |
      | checkout    | 2026-10-14           |
      | email       | divyaraj@gmail.com   |
      | phone       | 987542467405         |
    When I attempt to delete the booking without an authentication token
    Then the request should be rejected as unauthorized
