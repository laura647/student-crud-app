package com.example;

import com.example.dao.StudentDAO;
import com.example.model.Student;

public class Main {
    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();

        // Create a new student
        Student newStudent = new Student(0, "John", "Doe", 20);
        studentDAO.createStudent(newStudent);

        // Read all students
        System.out.println("\nAll Students:");
        for (Student student : studentDAO.getAllStudents()) {
            System.out.println(student);
        }

        // Read a specific student
        System.out.println("\nStudent with ID 1:");
        Student student = studentDAO.getStudentById(1);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }

        // Update a student
        if (student != null) {
            student.setFirstName("Jane");
            student.setAge(21);
            studentDAO.updateStudent(student);
        }

        // Read all students again
        System.out.println("\nAll Students after update:");
        for (Student s : studentDAO.getAllStudents()) {
            System.out.println(s);
        }

        // Delete a student
        studentDAO.deleteStudent(1);

        // Read all students again
        System.out.println("\nAll Students after deletion:");
        for (Student s : studentDAO.getAllStudents()) {
            System.out.println(s);
        }
    }
}