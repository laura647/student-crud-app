package com.example;

import javax.swing.*;
import com.example.UI.StudentUI;

/**
 * Main class to launch the Student Management System GUI application
 * This is the entry point for the graphical user interface version
 */
public class MainGUI {
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | 
                 InstantiationException | IllegalAccessException e) {
            // If system look and feel is not available, use default
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Create and display the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Show a splash message
                    System.out.println("Starting Student Management System GUI...");
                    
                    // Create and show the main application window
                    StudentUI app = new StudentUI();
                    app.setVisible(true);
                    
                    System.out.println("GUI application started successfully!");
                } catch (Exception e) {
                    // Show error dialog if application fails to start
                    JOptionPane.showMessageDialog(
                        null,
                        "Error starting the application: " + e.getMessage() + 
                        "\n\nPlease check:\n" +
                        "1. Database connection settings\n" +
                        "2. MySQL server is running\n" +
                        "3. Required dependencies are in classpath",
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}