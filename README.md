# Loan Payment Processing Service

A Spring Boot application that manages loans and their associated payments using a clean architecture with clear separation between **domain services** and an **application layer**.

---

## Architecture Overview

This project is structured into two core **Domain Services** and one **Application Layer**:

### Payment Domain
Handles the creation and validation of loan payments. This service ensures:
- Payments are within valid limits (no overpayment).
- Payment data is correctly associated with a loan.
- Payments are persisted through the `PaymentRepository`.

### Loan Domain
Handles loan-specific logic, such as:
- Tracking remaining balance.
- Updating loan status to `SETTLED` when fully paid.
- Retrieving loans via the `LoanRepository`.

### Application Layer (Processing Service)
Acts as an orchestrator that:
- Validates cross-domain rules.
- Retrieves the correct `Loan` entity using the `LoanDomainService`.
- Delegates payment creation to the `PaymentDomainService`.
- Returns enriched responses with loan and payment data.

This layer ensures **separation of concerns**, minimizing coupling between the domain services.

---

## Running the Application

### Prerequisites

Make sure you have the following installed:

- **Java 17+**
- **Maven 3.6+**
- IDE (optional but recommended): IntelliJ IDEA / Eclipse / VS Code

### Dependency Management

Dependencies are managed via **Maven**. To install them:

```bash
mvn clean install
```

### Start the App

```bash
mvn spring-boot:run
```
Or use your favorite IDE set up for Spring applications

The app will start on:

```
http://localhost:8080
```

---

## Testing the App

Unit tests are written using **JUnit 5** and mock dependencies with **Mockito**.

Run all tests using:

```bash
mvn test
```

---

## API & Tools

### Swagger API Docs

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html#/
```

###  H2 Database Console

Access the in-memory H2 database UI at:

```
http://localhost:8080/h2-console
```

Use the following credentials to log in:

- **JDBC URL**: Get the generated DB Connection String from the console output when starting the application
- **Username**: `sa`
- **Password**: *(leave blank)*

---

## Project Structure

```
src/
 └── main/
     └── java/
         └── com.bancx/
             ├── application/          # Application layer (orchestrator logic)
             ├── loan/                 # Loan domain logic
             └── payment/              # Payment domain logic
 └── test/
     └── java/
         └── com.bancx/
             └── application/          # Unit tests for ProcessingService
             └── loan/                 # Unit tests for LoanDomainService
             └── payment/              # Unit tests for PaymentDomainService
```

---

## Design Highlights

- Applies **Separation of Concerns** between domains.
- Uses **Spring Data JPA** for persistence.
- Enables rapid prototyping with **H2 in-memory DB**.
- Clean layering between **Domain Services** and **Application Orchestration**.

---