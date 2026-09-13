# API Testing Observations — Restful-Booker Platform
### Booking API — automationintesting.online

This report documents discrepancies found between the documented
`booking.yaml` OpenAPI spec / Swagger UI and the actual behavior of
the live API. Each item includes steps to reproduce and the captured
request/response evidence where available.

---

## 1. Swagger / Spec Documentation

### 1.1 Incorrect base URL in Swagger/spec

**Description:** The server URL documented in the OpenAPI spec does not match the actual base path used by the live API.

**Steps to Reproduce:**
1. Open the Swagger UI / `booking.yaml` spec and compare the documented `servers.url` against the actual base path required for requests to succeed.

**Expected Result:** Documented server URL should be directly usable as the base path for all endpoints.

**Actual Result:** `/api` is incorrectly appended to the documented server URL and must be added manually for requests to resolve correctly.

---

## 2. Create Booking — `POST /booking`

### 2.1 Response schema does not match documentation

**Description:** The response body structure does not match the `Booking` schema documented for this endpoint.

**Steps to Reproduce:**
1. Send a `POST /booking` request with a valid booking payload.
2. Inspect the response body structure.

**Request Body:**
```json
{
  "roomid": 1244,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2026-10-11",
    "checkout": "2026-10-12"
  },
  "email": "testnew@gmail.com",
  "phone": "987542467800"
}
```

**Expected Result:** Response nested under a `booking` object per the documented schema, including `email` and `phone`:
```json
{
  "bookingid": 4,
  "booking": {
    "roomid": 1244,
    "firstname": "Vishnu",
    "lastname": "Priya",
    "depositpaid": true,
    "bookingdates": { "checkin": "2026-10-11", "checkout": "2026-10-12" },
    "email": "testnew@gmail.com",
    "phone": "987542467800"
  }
}
```

**Actual Result:** Response is flat (no `booking` wrapper); `email` and `phone` are omitted entirely:
```json
{
  "bookingdates": { "checkin": "2026-10-11", "checkout": "2026-10-12" },
  "bookingid": 4,
  "depositpaid": true,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "roomid": 1244
}
```

---

### 2.2 Success status code mismatch

**Description:** The documented success status code for a valid booking creation does not match actual behavior.

**Steps to Reproduce:** Same as 2.1 above.

**Request Body:** Same as 2.1.

**Expected Result:** `200 OK`

**Actual Result:** `201 Created`

---

### 2.3 Incorrect validation error message for `lastname`

**Description:** When `lastname` fails minimum-length validation, the error message text does not match the spec's documented constraint. Note: the same check on `firstname` returns the correct message, so this is specific to `lastname`.

**Steps to Reproduce:**
1. Send a `POST /booking` request with `lastname` set to a 2-character value (below the minimum length).
2. Inspect the returned validation error message.

**Request Body:**
```json
{
  "roomid": 1909,
  "firstname": "Vishnu",
  "lastname": "Pr",
  "depositpaid": true,
  "bookingdates": { "checkin": "2026-10-16", "checkout": "2026-10-17" },
  "email": "pr@test.com",
  "phone": "98764253663"
}
```

**Expected Result:** `400 Bad Request` with error message: `"size must be between 3 and 18"`

**Actual Result:** `400 Bad Request` with error message:
```json
{
  "errors": ["size must be between 3 and 30"]
}
```

---

### 2.4 `lastname` maximum length constraint not enforced as documented

**Description:** A `lastname` value between 19 and 30 characters — which should be rejected per the spec's documented max of 18 — is accepted and the booking is created successfully. The API's actual constraint appears to be 3–30 characters, not 3–18 as documented.

**Steps to Reproduce:**
1. Send a `POST /booking` request with `lastname` set to a 20-character value.
2. Inspect the response.

**Request Body:**
```json
{
  "roomid": 1110,
  "firstname": "Vishnu",
  "lastname": "Priyatestlastnameerr",
  "depositpaid": true,
  "bookingdates": { "checkin": "2026-10-16", "checkout": "2026-10-17" },
  "email": "pr@test.com",
  "phone": "98764253663"
}
```

**Expected Result:** `400 Bad Request` with error message: `"size must be between 3 and 18"` (lastname exceeds the documented 18-character maximum).

**Actual Result:** `201 Created` — the booking is created without any validation error:
```json
{
  "bookingdates": { "checkin": "2026-10-16", "checkout": "2026-10-17" },
  "bookingid": 8,
  "depositpaid": true,
  "firstname": "Vishnu",
  "lastname": "Priyatestlastnameerr",
  "roomid": 1110
}
```

---

### 2.5 Incorrect status code for invalid date range

**Description:** When `checkout` date is earlier than `checkin` date, the returned status code does not match the documented behavior. The error response also uses a different structure (`"error"`, singular) than other validation failures (`"errors"`, an array).

**Steps to Reproduce:**
1. Send a `POST /booking` request where `bookingdates.checkout` is earlier than `bookingdates.checkin`.
2. Inspect the response status code and body.

**Request Body:**
```json
{
  "roomid": 1946,
  "firstname": "Vishnu",
  "lastname": "priya",
  "depositpaid": false,
  "bookingdates": { "checkin": "2026-10-18", "checkout": "2026-10-17" },
  "email": "vish@test.com",
  "phone": "987654321450"
}
```

**Expected Result:** `400 Bad Request` with error message: `"Failed to create booking"`

**Actual Result:** `409 Conflict`:
```json
{
  "error": "Failed to create booking"
}
```

---

## 3. Retrieve Booking — `GET /booking/{id}`

### 3.1 Incorrect status code for missing authentication

**Description:** Requesting a booking without an authentication token returns an incorrect status code.

**Steps to Reproduce:**
1. Create a booking.
2. Send a `GET /booking/{id}` request for that booking id **without** an authentication cookie/token.

**Expected Result:** `401 Unauthorized`

**Actual Result:** `403 Forbidden`

---

### 3.2 Response schema mismatch

**Description:** The response does not match the documented `Booking` schema — required fields `email` and `phone` are missing, and an undocumented `bookingid` field is present at the top level (the `Booking` schema has no such field, since the id is already known from the URL path).

**Steps to Reproduce:**
1. Create a booking.
2. Send an authenticated `GET /booking/{id}` request for that booking id.
3. Inspect the response body.

**Expected Result:** Response matches the `Booking` schema exactly, including `email` and `phone`, with no `bookingid` field:
```json
{
  "roomid": 1355,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "depositpaid": true,
  "bookingdates": { "checkin": "2026-10-11", "checkout": "2026-10-12" },
  "email": "testnew@gmail.com",
  "phone": "987542467800"
}
```

**Actual Result:** `email` and `phone` are omitted; an extra `bookingid` field is present instead:
```json
{
  "bookingdates": { "checkin": "2026-10-11", "checkout": "2026-10-12" },
  "bookingid": 4,
  "depositpaid": true,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "roomid": 1355
}
```

---

## 4. Update Booking — `PUT /booking/{id}`

### 4.1 Response schema mismatch

**Description:** The `PUT` response does not match the documented `Booking` schema — the booking data is nested under a `booking` object, with `bookingid` duplicated at the top level.

**Steps to Reproduce:**
1. Create a booking.
2. Log in and obtain an authentication token.
3. Send an authenticated `PUT /booking/{id}` request with updated booking details.
4. Inspect the response body structure.

**Request Body:**
```json
{
  "roomid": 1393,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "depositpaid": false,
  "bookingdates": {
    "checkin": "2026-10-15",
    "checkout": "2026-10-18"
  },
  "email": "updated@gmail.com",
  "phone": "912233445566"
}
```

**Expected Result:** Response matches the flat `Booking` schema, per `booking.yaml`:
```json
{
  "roomid": 1393,
  "firstname": "Vishnu",
  "lastname": "Priya",
  "depositpaid": false,
  "bookingdates": { "checkin": "2026-10-15", "checkout": "2026-10-18" },
  "email": "updated@gmail.com",
  "phone": "912233445566"
}
```

**Actual Result:** Response nested under a `booking` object, with `bookingid` also duplicated at the top level:
```json
{
  "booking": {
    "bookingdates": { "checkin": "2026-10-15", "checkout": "2026-10-18" },
    "bookingid": 7,
    "depositpaid": false,
    "firstname": "Vishnu",
    "lastname": "Priya",
    "roomid": 1393
  },
  "bookingid": 7
}
```

---

### 4.2 Incorrect status code for missing authentication

**Description:** Same as 3.1, observed on the `PUT` endpoint.

**Steps to Reproduce:**
1. Create a booking.
2. Send a `PUT /booking/{id}` request with updated details, **without** an authentication token.

**Expected Result:** `401 Unauthorized`

**Actual Result:** `403 Forbidden`

---

## 5. Partially Update Booking — `PATCH /booking/{id}`

### 5.1 Endpoint does not support PATCH

**Description:** The documented `PATCH` operation is not implemented on the live API; the method is rejected outright, even with valid authentication.

**Steps to Reproduce:**
1. Create a booking.
2. Log in and obtain an authentication token.
3. Send an authenticated `PATCH /booking/{id}` request with a partial payload.

**Request Body:**
```json
{
  "firstname": "Priyanka",
  "depositpaid": false
}
```

**Expected Result:** `200 OK`, with the updated booking returned in the response body.

**Actual Result:** `405 Method Not Allowed`:
```json
{
  "timestamp": "2026-09-13T08:30:13.631Z",
  "status": 405,
  "error": "Method Not Allowed",
  "path": "/booking/5"
}
```

---

### 5.2 Auth behavior cannot be verified for PATCH

**Description:** Since the method is rejected before authentication is evaluated, the documented `401` behavior for missing auth cannot be confirmed on this endpoint.

**Steps to Reproduce:**
1. Create a booking.
2. Send a `PATCH /booking/{id}` request with a partial payload, **without** an authentication token.

**Request Body:**
```json
{
  "firstname": "Uma"
}
```

**Expected Result:** `401 Unauthorized`

**Actual Result:** `405 Method Not Allowed`:
```json
{
  "timestamp": "2026-09-13T08:30:15.716Z",
  "status": 405,
  "error": "Method Not Allowed",
  "path": "/booking/6"
}
```

---

## 6. Delete Booking — `DELETE /booking/{id}`

### 6.1 Incorrect success status code

**Description:** The documented success status code for a booking deletion does not match actual behavior.

**Steps to Reproduce:**
1. Create a booking.
2. Log in and obtain an authentication token.
3. Send an authenticated `DELETE /booking/{id}` request.

**Expected Result:** `201 Created` (as documented, despite being an unconventional choice for a delete operation).

**Actual Result:** `202 Accepted` (response body empty).

---

### 6.2 Incorrect status code for missing authentication

**Description:** Same pattern as 3.1 and 4.2, observed on the `DELETE` endpoint.

**Steps to Reproduce:**
1. Create a booking.
2. Send a `DELETE /booking/{id}` request **without** an authentication token.

**Expected Result:** `401 Unauthorized`

**Actual Result:** `403 Forbidden` (response body empty).

---

## Test Execution Summary (Automated Suite Run)

Latest full suite run: **20 scenarios (5 passed, 15 failed)**, **119 steps (98 passed, 6 skipped, 15 failed)**, ~58s.

All 15 failures correspond to already-documented findings above — no failures are unexplained:

| Feature | Scenario | Failure maps to |
|---|---|---|
| create_booking | Smoke: create booking | 2.1 (schema) |
| create_booking | Outline: 3 examples | 2.1 (schema), ×3 |
| create_booking | lastname too long → error message | 2.3 |
| create_booking | lastname too long → wrongly accepted | 2.4 |
| create_booking | invalid date range | 2.5 |
| delete_booking | Successful delete | 6.1 (202 vs 201) |
| delete_booking | Unauthorized delete | 6.2 (403 vs 401) |
| fetch_booking | Successful retrieve → schema | 3.2 (schema mismatch) |
| fetch_booking | Unauthorized retrieve | 3.1 (403 vs 401) |
| patch_booking | Successful patch | 5.1 (405, not implemented) |
| patch_booking | Unauthorized patch | 5.2 (405 masks auth check) |
| update_booking | Successful update → detail match | 4.1 (schema mismatch) |
| update_booking | Unauthorized update | 4.2 (403 vs 401) |

---

## Notes
- All observations are now confirmed via automated Cucumber/REST Assured
  test execution against the live environment (see Test Execution Summary
  above), not just manual verification.
- Re-verify after any API or spec updates.
- `PATCH` support should be confirmed with the API team before deciding
  whether to keep, skip, or mark the corresponding test scenarios as
  expected-to-fail.
