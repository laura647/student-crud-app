Student CRUD Application
This is a Java application that performs CRUD (Create, Read, Update, Delete) operations on a MySQL database for managing student records.
Prerequisites

Java 17 or later
Maven
MySQL database with the school_db database and students table

Database Schema
See src/main/resources/schema.sql for the proposed schema:

Database: school_db
Table: students with columns:
id (INT, auto-increment, primary key)
firstName (VARCHAR)
lastName (VARCHAR)
age (INT)



Update src/main/java/com/example/db/DatabaseConnection.java with your MySQL credentials:

URL: jdbc:mysql://localhost:3306/school_db?useSSL=false
Username
Password

Setup

Clone the repository:git clone https://github.com/laura647/student-crud-app.git


Navigate to the project directory:cd student-crud-app


Install dependencies:mvn clean install
