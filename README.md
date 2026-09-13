# API Testing Kata

A Cucumber + REST Assured based API test automation framework for the
**Restful-Booker Platform** booking API
([automationintesting.online](https://automationintesting.online)).

Covers full CRUD lifecycle testing for the `/booking` resource, including
authentication, positive and negative scenarios, JSON schema validation
against the documented OpenAPI spec, and automated HTML reporting.

---

## Tech Stack

| Category | Tool |
|---|---|
| Language | Java 17 |
| BDD Framework | Cucumber-JVM 7.34.6 |
| API Client | REST Assured 6.0.1 |
| Test Runner | JUnit 5 (JUnit Platform Suite) |
| Dependency Injection | Cucumber PicoContainer |
| JSON Mapping | Jackson Databind |
| Schema Validation | REST Assured JSON Schema Validator |
| Logging | SLF4J |
| Reporting | Masterthought Cucumber Reporting |
| Build Tool | Maven |

---

## Project Structure

```
API_Testing_Kata/
├── pom.xml
└── src/test/
    ├── java/com/booking/
    │   ├── TestRunner.java              # JUnit 5 Suite entry point
    │   ├── models/                      # Request/response data (Java records)
    │   ├── stepdefinitions/             # Gherkin step implementations
    │   ├── utils/                       # Config, API actions, assertions, context
    │   └── reporting/                   # Automatic HTML report generation
    └── resources/
        ├── config/config.properties     # Base URL, endpoints, credentials
        ├── features/                    # Gherkin feature files
        ├── schemas/                     # JSON schemas (per booking.yaml spec)
        └── META-INF/services/           # ServiceLoader registration
```

---

## Prerequisites

- JDK 17+
- Maven 3.9+ (or use IntelliJ's bundled Maven)
- IntelliJ IDEA (recommended) with the Cucumber for Java plugin

---

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/VishnupriyaG-tech/API_Testing_Kata.git
   ```
2. Open in IntelliJ as a Maven project (or run `mvn compile` from the terminal).
3. Confirm `src/test/java` is marked as **Test Sources Root** and
   `src/test/resources` as **Test Resources Root**.

---

## Configuration

Base URL, endpoint paths, and auth credentials are set in:
```
src/test/resources/config/config.properties
```

---

## Running the Tests

### Run everything
Right-click `TestRunner.java` → **Run**, or:
```bash
mvn test
```

### Run by tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@smoke or @regression"
mvn test -Dcucumber.filter.tags="@positive and not @smoke"
```

Available tags: `@smoke`, `@regression`, `@positive`, `@negative`, `@auth`.

---

## Feature Coverage

| Feature file | Endpoint | Scenarios |
|---|---|---|
| `create_booking.feature` | `POST /booking` | Positive (smoke + outline), negative validation |
| `fetch_booking.feature` | `GET /booking/{id}` | Positive with auth, negative without auth |
| `update_booking.feature` | `PUT /booking/{id}` | Positive with auth, negative without auth |
| `patch_booking.feature` | `PATCH /booking/{id}` | Positive with auth, negative without auth |
| `delete_booking.feature` | `DELETE /booking/{id}` | Positive with auth, negative without auth |

---

## Reporting

An HTML dashboard is generated **automatically** after every test run — no
extra command needed. This works whether you run via IntelliJ's Run button
or via Maven, thanks to a `TestExecutionListener` registered via
`ServiceLoader`.

Open the report at:
```
target/cucumber-html-reports/overview-features.html
```

---

## Known API Discrepancies

The live API deviates from its own documented `booking.yaml` OpenAPI spec
in several places (status codes, response shapes, missing fields, and an
unimplemented `PATCH` method). Full details, including repro steps and
captured request/response bodies, are documented in:
```
api_testing_observations.md
```

Schema validation steps are written against the **documented spec**, not
observed behavior — so several schema-adherence assertions are expected to
fail until the API is brought in line with its own documentation.

---

## Commit Message Convention

Commits follow [Conventional Commits](https://www.conventionalcommits.org/)
style types (`feat`, `fix`, `refactor`, `test`, `chore`, `docs`), with a
short imperative subject line and a brief body explaining what changed.

---

## Author

**Vishnupriya G**

