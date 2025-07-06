package com.example.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.example.dao.StudentDAO;
import com.example.model.Student;

public class StudentUI extends JFrame {
    private StudentDAO studentDAO;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField firstNameField, lastNameField, ageField, searchField;
    private JButton addButton, updateButton, deleteButton, refreshButton, searchButton, clearButton;
    private Student selectedStudent;

    public StudentUI() {
        studentDAO = new StudentDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        refreshTable();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Input fields
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        ageField = new JTextField(15);
        searchField = new JTextField(15);

        // Buttons
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Search by ID");
        clearButton = new JButton("Clear Fields");

        // Table
        String[] columnNames = {"ID", "First Name", "Last Name", "Age"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style the table
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setBackground(new Color(70, 130, 180));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();

        // Combine input and button panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        // Add search panel
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));

        // Create north panel combining input and search
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.CENTER);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // First Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        panel.add(lastNameField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Student ID:"));
        panel.add(searchField);
        panel.add(searchButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Students List"));
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventListeners() {
        // Add button
        addButton.addActionListener(e -> addStudent());

        // Update button
        updateButton.addActionListener(e -> updateStudent());

        // Delete button
        deleteButton.addActionListener(e -> deleteStudent());

        // Refresh button
        refreshButton.addActionListener(e -> refreshTable());

        // Search button
        searchButton.addActionListener(e -> searchStudent());

        // Clear button
        clearButton.addActionListener(e -> clearFields());

        // Table selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFieldsFromSelectedRow(selectedRow);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    clearFields();
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });
    }

    private void addStudent() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String ageText = ageField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty()) {
                showMessage("Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 150) {
                showMessage("Please enter a valid age (1-150).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(0, firstName, lastName, age);
            studentDAO.createStudent(student);
            
            showMessage("Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid number for age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showMessage("Error adding student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (selectedStudent == null) {
            showMessage("Please select a student to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String ageText = ageField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty()) {
                showMessage("Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 150) {
                showMessage("Please enter a valid age (1-150).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedStudent.setFirstName(firstName);
            selectedStudent.setLastName(lastName);
            selectedStudent.setAge(age);

            studentDAO.updateStudent(selectedStudent);
            
            showMessage("Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid number for age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showMessage("Error updating student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (selectedStudent == null) {
            showMessage("Please select a student to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete " + selectedStudent.getFirstName() + " " + selectedStudent.getLastName() + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentDAO.deleteStudent(selectedStudent.getId());
                showMessage("Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } catch (Exception ex) {
                showMessage("Error deleting student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchStudent() {
        try {
            String idText = searchField.getText().trim();
            if (idText.isEmpty()) {
                showMessage("Please enter a student ID to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            Student student = studentDAO.getStudentById(id);

            if (student != null) {
                // Clear table and show only found student
                tableModel.setRowCount(0);
                Object[] rowData = {student.getId(), student.getFirstName(), student.getLastName(), student.getAge()};
                tableModel.addRow(rowData);
                
                // Select the row
                studentTable.setRowSelectionInterval(0, 0);
                
                showMessage("Student found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessage("No student found with ID: " + id, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            }
            
            searchField.setText("");
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid number for student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showMessage("Error searching student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            
            for (Student student : students) {
                Object[] rowData = {student.getId(), student.getFirstName(), student.getLastName(), student.getAge()};
                tableModel.addRow(rowData);
            }
            
            clearFields();
        } catch (Exception ex) {
            showMessage("Error refreshing table: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFieldsFromSelectedRow(int row) {
        try {
            int id = (Integer) tableModel.getValueAt(row, 0);
            String firstName = (String) tableModel.getValueAt(row, 1);
            String lastName = (String) tableModel.getValueAt(row, 2);
            int age = (Integer) tableModel.getValueAt(row, 3);

            selectedStudent = new Student(id, firstName, lastName, age);
            
            firstNameField.setText(firstName);
            lastNameField.setText(lastName);
            ageField.setText(String.valueOf(age));
        } catch (Exception ex) {
            showMessage("Error loading student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        selectedStudent = null;
        studentTable.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new StudentUI().setVisible(true);
        });
    }
}