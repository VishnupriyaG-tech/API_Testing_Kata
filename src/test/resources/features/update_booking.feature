Background:
Given the Booking service is up and running

@smoke @positive
Scenario: Successfully update an existing booking with valid authentication
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
When I update the booking with the following details:
| firstname   | Vishnu             |
| lastname    | Priya               |
| depositpaid | false                |
| checkin     | 2026-10-15           |
| checkout    | 2026-10-18           |
| email       | updated@gmail.com    |
| phone       | 912233445566         |
Then the booking should be updated successfully
And the updated booking details should reflect the submitted changes

@regression @negative @auth
Scenario: Fail to update a booking without providing an authentication token
Given a guest wants to reserve a room
And a booking has been created with the following details:
| firstname   | John             |
| lastname    | Smith               |
| depositpaid | true                 |
| checkin     | 2026-10-11           |
| checkout    | 2026-10-12           |
| email       | testnew@gmail.com    |
| phone       | 987541097800         |
When I attempt to update the booking without an authentication token with the following details:
| firstname   | John             |
| lastname    | Kennedy               |
| depositpaid | false                |
| checkin     | 2026-10-15           |
| checkout    | 2026-10-18           |
| email       | updated@gmail.com    |
| phone       | 912100005566         |
Then the request should be rejected as unauthorized
