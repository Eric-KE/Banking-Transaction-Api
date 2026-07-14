# Core Banking Transaction Processing API
A high-performance backend system built with Spring Boot for managing User accounts, Multi-currency ledgers, and concurrent financial transactions. Designed to demonstrate real world backend engineering practices including transaction isolation, race condition prevention, centralized error handling, and automated schema migrations.

## Features
-User registration with auto-generated customer numbers.

-Multi-currency account support (KES, USD, GBP, EUR).

-Bank style account number generation(branch + product + customer code).

-Deposits, withdrawals and transfers with full balance validation.

-Pessimistic locking and configurable transaction isolation levels to prevent race condition.

-centralized exception handling with structured, logged error responses.

-Automated, version-controlled database schema migrations via Flyway.

## Tech Stack
Java, Spring Boot, Spring Data JPA, PostgreSQL, Flyway, Maven

## Architecture Highlights
**Concurrency & Data Intergrity**

Pessimistic write locks (`SELECT ... FROM UPDATE`) combine with `REPEATABLE_READ` isolation prevent race conditions on account balances. Verified with a Multi-threaded test simulating simultaneous withdrawals on the same account.

**Error Handling**

A global `@RestControllerAdvice` handler catches domian specific exceptiions(`ResourceNotFoundException`, `InsufficientFundsException`, `DuplicateResourceException`) and validation failures, returning consistent JSON error responses with appropriate HTTP status codes and logging every failure automatically.

**Schema Management**

Flyway manages all schema changes as version controlled SQL migration files, ensuring local and staging environements stay in sync rather than relying on ORM auto generation.

##Getting Started
### Prerequisites

-Java 25 (or your installed JDK version)

-maven (or use the included './mvnw' wrapper)

-PostgreSQL 14+ installed and running

###Setup

1.** Clone the repo**
```bash
git clone https://github.com/Eric-KE/Banking-Transaction-Api.git
cd Banking-Transaction-Api
```

2.** Create the Database **
```sql
  CREATE DATABASE ledger_db;
  CREATE USER ledger_user WITH PASSWORD 'yourpassword';
  GRANT ALL PRIVILEGES ON DATABASE ledger_db TO ledger_user;
```
3.** Configure the connection**
Edit `src/main/resources/appication.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ledger_db
spring.datasource.username=ledger_user
spring.datasource.password=yourpassword
```
4.**Run the appication**
```bash
./mvnw spring-boot:run
```

## API Endpoints
Post
`api/v1/users` - Register a new username

GET
`/api/v1/users` - List all users

GET
`/api/v1/user/{id}` - Get a user by ID

POST
`/api/v1/accounts` - Open a new currency account

GET
`/api/v1/accounts/{id}` - Get an account by ID

GET
`/api/v1/accounts/user/{userId}` - List all accounts for a user

POST
`/api/v1/ledger/deposit`  -Deposit funds into an account

POST
`/api/v1/ledger/withdraw` - Withdrawal funds from an account

POST
`/api/v1/ledger/transfer` - Transfer funds between accounts

GET
`/api/v1/ledger/transactions/{accountId}` - View transaction history for an account

## Database Schema

**users** - id, full_name, email, customer_number, created_at

**accounts** - id, account_number, currency, balance, user_id(FK), created_at

**transactions** - id, type, amount, status, source_account_id (FK), destination_account_id (Fk), created_at, failure_reason

##Future Enhancements

-Multi-currency transfers with live exchange rate conversion via an external API.

-Multi-branch account support.

-Authentication and authorization (JWT-based).

-Pagination for list endpoints.

-Automated integration test suite.

-Connection with a frontend.

## Lincense
This project is for educational purposes
