@API @Booking @E2E
Feature: End-to-End Room Reservation Lifecycle
  As a user
  I want to create, retrieve, update, and delete a booking in one continuous flow
  So that I can confirm the full booking lifecycle works correctly end to end

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully create, retrieve, update, and delete a booking
    Given a guest wants to reserve a room
    When I create a booking with the following details:
      | firstname   | Vishnu            |
      | lastname    | Priya             |
      | depositpaid | true              |
      | checkin     | 2026-10-20        |
      | checkout    | 2026-10-22        |
      | email       | testnew@gmail.com |
      | phone       | 987542467800      |
    Then the booking should be created successfully
    And the response should contain a valid booking id

    Given the user has logged in with valid credentials
    When I retrieve the booking using its booking id
    Then the booking should be retrieved successfully
    And the retrieved booking details should match the originally submitted details

    When I update the booking with the following details:
      | firstname   | Vishnu            |
      | lastname    | Priya             |
      | depositpaid | false             |
      | checkin     | 2026-10-25        |
      | checkout    | 2026-10-28        |
      | email       | updated@gmail.com |
      | phone       | 912233445566      |
    Then the booking should be updated successfully
    And the updated booking details should reflect the submitted changes

    When I delete the booking
    Then the booking should be deleted successfully
    And the booking should no longer be retrievable
