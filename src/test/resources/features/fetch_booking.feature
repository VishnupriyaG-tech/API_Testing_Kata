@API @Booking @Auth
Feature: Retrieve Room Reservation Details
  As a user
  I want to retrieve the details of an existing booking using its booking id
  So that I can confirm the reservation information on record

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully retrieve an existing booking with valid authentication
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Vishnu             |
      | lastname    | Priya               |
      | depositpaid | true                 |
      | checkin     | 2026-10-11           |
      | checkout    | 2026-10-12           |
      | email       | testnew@gmail.com    |
      | phone       | 987542467800         |
    And the user has logged in with valid credentials
    When I retrieve the booking using its booking id
    Then the booking should be retrieved successfully
    And the retrieved booking details should match the originally submitted details

  @regression @negative @auth
  Scenario: Fail to retrieve a booking without providing an authentication token
    Given a guest wants to reserve a room
    And a booking has been created with the following details:
      | firstname   | Vishnu             |
      | lastname    | Priya               |
      | depositpaid | true                 |
      | checkin     | 2026-10-11           |
      | checkout    | 2026-10-12           |
      | email       | testnew@gmail.com    |
      | phone       | 987542467800         |
    When I attempt to retrieve the booking without an authentication token
    Then the request should be rejected as unauthorized


