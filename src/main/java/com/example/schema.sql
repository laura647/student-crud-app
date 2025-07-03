-- Proposed MySQL schema for the student-crud-app
-- Please create this database and table, or let me know if you modify the schema so I can update the Java code.

CREATE DATABASE school_db;
USE school_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    age INT NOT NULL
);