package com.example;

import java.util.Scanner;

import com.example.dao.StudentDAO;
import com.example.model.Student;

/**
 * Console-based main class for the Student Management System
 * This provides a command-line interface for CRUD operations
 */
public class Main {
    private static StudentDAO studentDAO = new StudentDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Student Management System ===");
        System.out.println("Console Version");
        System.out.println("==================================");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    createStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    running = false;
                    System.out.println("Thank you for using Student Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. View Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private static int getChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void createStudent() {
        System.out.println("\n=== ADD NEW STUDENT ===");
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Enter Age: ");
        int age = 0;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Operation cancelled.");
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || age <= 0) {
            System.out.println("Invalid input. All fields are required and age must be positive.");
            return;
        }

        Student student = new Student(0, firstName, lastName, age);
        studentDAO.createStudent(student);
        System.out.println("Student added successfully!");
    }

    private static void viewAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        var students = studentDAO.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.printf("%-5s %-15s %-15s %-5s%n", "ID", "First Name", "Last Name", "Age");
            System.out.println("---------------------------------------------");
            for (Student student : students) {
                System.out.printf("%-5d %-15s %-15s %-5d%n", 
                    student.getId(), 
                    student.getFirstName(), 
                    student.getLastName(), 
                    student.getAge());
            }
        }
    }

    private static void viewStudentById() {
        System.out.println("\n=== VIEW STUDENT BY ID ===");
        System.out.print("Enter Student ID: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Student student = studentDAO.getStudentById(id);
            
            if (student != null) {
                System.out.println("\nStudent found:");
                System.out.printf("ID: %d%n", student.getId());
                System.out.printf("Name: %s %s%n", student.getFirstName(), student.getLastName());
                System.out.printf("Age: %d%n", student.getAge());
            } else {
                System.out.println("No student found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void updateStudent() {
        System.out.println("\n=== UPDATE STUDENT ===");
        System.out.print("Enter Student ID to update: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Student student = studentDAO.getStudentById(id);
            
            if (student == null) {
                System.out.println("No student found with ID: " + id);
                return;
            }

            System.out.println("Current student information:");
            System.out.printf("Name: %s %s, Age: %d%n", 
                student.getFirstName(), student.getLastName(), student.getAge());
            
            System.out.print("Enter new First Name (current: " + student.getFirstName() + "): ");
            String firstName = scanner.nextLine().trim();
            if (!firstName.isEmpty()) {
                student.setFirstName(firstName);
            }
            
            System.out.print("Enter new Last Name (current: " + student.getLastName() + "): ");
            String lastName = scanner.nextLine().trim();
            if (!lastName.isEmpty()) {
                student.setLastName(lastName);
            }
            
            System.out.print("Enter new Age (current: " + student.getAge() + "): ");
            String ageStr = scanner.nextLine().trim();
            if (!ageStr.isEmpty()) {
                try {
                    int age = Integer.parseInt(ageStr);
                    if (age > 0) {
                        student.setAge(age);
                    } else {
                        System.out.println("Age must be positive. Keeping current age.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age format. Keeping current age.");
                }
            }
            
            studentDAO.updateStudent(student);
            System.out.println("Student updated successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void deleteStudent() {
        System.out.println("\n=== DELETE STUDENT ===");
        System.out.print("Enter Student ID to delete: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Student student = studentDAO.getStudentById(id);
            
            if (student == null) {
                System.out.println("No student found with ID: " + id);
                return;
            }

            System.out.printf("Are you sure you want to delete student: %s %s (ID: %d)? (y/N): ",
                student.getFirstName(), student.getLastName(), student.getId());
            
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (confirmation.equals("y") || confirmation.equals("yes")) {
                studentDAO.deleteStudent(id);
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Delete operation cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }
}
