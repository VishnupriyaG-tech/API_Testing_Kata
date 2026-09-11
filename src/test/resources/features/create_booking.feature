@API @Booking
Feature: Create a Room Reservation
  As a user
  I want to reserve a room by providing the requested information

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully create a booking with valid guest and stay details
    Given a room is available for reservation
    When I create a booking with the following details:
      | firstname   | Vishnu             |
      | lastname    | Priya               |
      | totalprice  | 140                  |
      | depositpaid | true                 |
      | checkin     | 2026-09-11           |
      | checkout    | 2026-09-12           |
      | email       | testnew@gmail.com    |
      | phone       | 987542467800         |
    Then the booking should be created successfully
    And the response should contain a valid booking id
    And the booking confirmation should reflect the submitted guest and stay details
    And the booking confirmation adheres to the expected response structure

  @regression @positive
  Scenario Outline: Successfully create bookings for guests with different deposit statuses and stay durations
    Given rooms are available for reservations
    When I create a booking with the following details:
      | firstname   | <firstname>   |
      | lastname    | <lastname>    |
      | depositpaid | <depositpaid> |
      | checkin     | <checkin>     |
      | checkout    | <checkout>    |
      | email       | <email>       |
      | phone       | <phone>       |
    Then the booking should be created successfully
    And the response should contain a valid booking id
    And the booking confirmation should reflect the submitted guest and stay details
    And the booking confirmation adheres to the expected response structure

    Examples: Deposit already paid
      | firstname | lastname | depositpaid  | checkin    | checkout   | email             | phone        |
      | Hariraja  | Subbu    | true         | 2026-09-11 | 2026-09-12 | testnew@gmail.com | 987542467800 |
      | Anita     | Sharma   | true         | 2026-11-05 | 2026-11-10 | anita.s@mail.com  | 912233445566 |

    Examples: Deposit pending
      | firstname | lastname | depositpaid | checkin    | checkout   | email              | phone        |
      | Ravi      | Kumar    | false        | 2026-10-01 | 2026-10-05 | ravi.kumar@mail.com| 998877665544 |

