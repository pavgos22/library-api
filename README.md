# Library Management System - REST API

## Overview

This Library Management System is a RESTful API built using Spring Boot 3.1.11, Hibernate, and MySQL. The API is designed to manage the operations of a library, focusing on managing readers, book titles, book copies, and loan transactions. It allows the creation of readers, book titles, and book copies, as well as managing book loans and returns. 

## Key Features

- **Reader Management**: Add readers with basic details such as first name, last name, and account creation date.
- **Title Management**: Add book titles, including title name, author, and publication year.
- **Book Copy Management**: Add and manage copies of book titles, track their status, and handle their lifecycle within the library.
- **Loan Management**: Manage the loan and return processes of book copies, ensuring that the system tracks which reader has which book.

## Entity Relationships

The system is built around four primary entities:

1. **Readers**:
   - `id` (Long): The unique identifier for a reader.
   - `first_name` (String): The reader's first name.
   - `last_name` (String): The reader's last name.
   - `account_creation_date` (Timestamp): The date the reader's account was created.

2. **Titles**:
   - `id` (Long): The unique identifier for a book title.
   - `title` (String): The name of the book.
   - `author` (String): The author of the book.
   - `year` (Integer): The publication year of the book.

3. **Books**:
   - `id` (Long): The unique identifier for a book copy.
   - `title_id` (Long): The foreign key linking the book to a title.
   - `status` (Enum): The status of the book copy (e.g., `AVAILABLE`, `RENTED`).

4. **Loans**:
   - `id` (Long): The unique identifier for a loan transaction.
   - `book_id` (Long): The foreign key linking the loan to a specific book copy.
   - `user_id` (Long): The foreign key linking the loan to a specific reader.
   - `borrow_date` (Timestamp): The date the book was borrowed.
   - `return_date` (Timestamp, nullable): The date the book was returned.

## Testing

The application is thoroughly tested using `JUnit` with a focus on unit and integration tests, leveraging an H2 in-memory database in the test profile to ensure robust functionality across REST controllers, services, and repository layers, verifying the correct behavior of all key features.

### Test Coverage

- **Test Coverage**: The project currently achieves a test coverage of **83%**, indicating that a significant portion of the codebase is covered by automated tests. This high level of coverage ensures that the application is well-tested and resilient to changes.

### Test Suites

1. **Controller Tests**: 
   - Tests the REST API endpoints to ensure they handle HTTP requests and responses correctly.
   - Includes tests for all major operations such as creating, retrieving, updating, and deleting entities (e.g., books, users, loans).
   - Uses `MockMvc` for simulating HTTP requests and verifying the behavior of the API controllers.

2. **Service Tests**:
   - Focuses on the business logic contained within the service layer.
   - Tests various service methods to ensure they perform the expected operations, such as creating entities, managing loans, and updating statuses.

3. **Entity Tests**:
   - Ensures the basic operations can be executed on database properly using EntityManager.
   - Verifies the correctness of relationships between entities in the database.

## Technologies Used

- **Spring Boot 3.1.11**: A powerful framework for building Java applications with embedded servers and simplified dependency management.
- **Hibernate**: ORM framework used to map the object-oriented domain model to the relational database.
- **MySQL**: Relational database management system used for storing all the data.
- **Gradle**: Dependency management and build tool.
- **JUnit**: Testing framework for unit tests.
- **H2**: Second level in-memory database for testing.

## How to run

1. Clone the repository or download the ZIP file
2. Run `./gradlew build` command to build the project
3. Open the project in your IDE and run the `JavaLibraryApplication.java` file.
4. The API will be accessible at `http://localhost:8080`.

