@API @Booking
Feature: Create a Room Reservation
  As a user
  I want to reserve a room by providing the requested information

  Background:
    Given the Booking service is up and running

  @smoke @positive
  Scenario: Successfully create a booking with valid guest and stay details
    Given a guest wants to reserve a room
    When I create a booking with the following details:
      | firstname   | Vishnu             |
      | lastname    | Priya               |
      | depositpaid | true                 |
      | checkin     | 2026-10-11           |
      | checkout    | 2026-10-12           |
      | email       | testnew@gmail.com    |
      | phone       | 987542467800         |
    Then the booking should be created successfully
    And the response should contain a valid booking id
    And the booking confirmation should reflect the submitted guest and stay details
    And the booking confirmation adheres to the expected response structure

  @regression @positive
  Scenario Outline: Successfully create bookings for guests with different deposit statuses and stay durations
    Given a guest wants to reserve a room
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
      | Hariraja  | Subbu    | true         | 2026-10-14 | 2026-10-15 | testnew@gmail.com | 987542467800 |
      | Anita     | Sharma   | true         | 2026-11-05 | 2026-11-10 | anita.s@mail.com  | 912233445566 |

    Examples: Deposit pending
      | firstname | lastname | depositpaid | checkin    | checkout   | email              | phone        |
      | Ravi      | Kumar    | false       | 2026-10-01 | 2026-10-05 | ravi.kumar@mail.com| 998877665544 |

  @negative @validation
  Scenario Outline: Fail to create a booking when invalid information are provided
    Given a guest wants to reserve a room
    When I create a booking with the incorrect requested details:
      | firstname   | <firstname>   |
      | lastname    | <lastname>    |
      | depositpaid | <depositpaid> |
      | checkin     | <checkin>     |
      | checkout    | <checkout>    |
      | email       | <email>       |
      | phone       | <phone>       |
    Then the system rejects the booking request due to validation errors
    And the validation error message indicates "<errorMessage>"

    Examples:
      | firstname              | lastname             | email         | phone                   | depositpaid | checkin    | checkout   | errorMessage                             |
      | Vi                     | Priya                | vp@test.com   | 98764253661             | true        | 2026-10-15 | 2026-10-16 | size must be between 3 and 18            |
      | Vishnupriyanewtestname | Priya                | vp@test.com   | 987642536623            | true        | 2026-10-15 | 2026-10-17 | size must be between 3 and 18            |
      | Vishnu                 | Pr                   | pr@test.com   | 98764253663             | true        | 2026-10-16 | 2026-10-17 | size must be between 3 and 18            |
      | Vishnu                 | Priyatestlastnameerr | pr@test.com   | 98764253663             | true        | 2026-10-16 | 2026-10-17 | size must be between 3 and 18            |
      | Vishnu                 | priya                | vish@test.com | 987654321               | true        | 2026-10-18 | 2026-10-19 | size must be between 11 and 21           |
      | Vishnu                 | priya                | vish@test.com | 98765432192839920092838 | false       | 2026-10-18 | 2026-10-19 | size must be between 11 and 21           |
      | Vishnu                 | priya                | vishtest.com  | 987654321453            | true        | 2026-10-18 | 2026-10-19 | must be a well-formed email address      |
      | Vishnu                 | priya                | vish@test.com | 987654321450            | false       | 2026-10-18 | 2026-10-17 | Failed to create booking                 |
