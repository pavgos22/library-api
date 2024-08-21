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
   - `status` (Enum): The status of the book copy (e.g., `AVAILABLE`, `RENTED`, `DESTROYED`, `LOST`).

4. **Loans**:
   - `id` (Long): The unique identifier for a loan transaction.
   - `book_id` (Long): The foreign key linking the loan to a specific book copy.
   - `user_id` (Long): The foreign key linking the loan to a specific reader.
   - `borrow_date` (Timestamp): The date the book was borrowed.
   - `return_date` (Timestamp, nullable): The date the book was returned.

## Technologies Used

- **Spring Boot 3.1.11**: A powerful framework for building Java applications with embedded servers and simplified dependency management.
- **Hibernate**: ORM framework used to map the object-oriented domain model to the relational database.
- **MySQL**: Relational database management system used for storing all the data.
- **Gradle**: Dependency management and build tool.
- **JUnit**: Testing framework for unit tests.

