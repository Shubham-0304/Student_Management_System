package com.sms;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based menu that drives the Student Management System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addStudent();
                case "2" -> viewAllStudents();
                case "3" -> viewStudentById();
                case "4" -> updateStudent();
                case "5" -> deleteStudent();
                case "6" -> searchStudents();
                case "7" -> {
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== Student Management System =====");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. View Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Search Students by Name");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Age: ");
        int age = readInt();

        System.out.print("Course: ");
        String course = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        Student student = new Student(name, age, course, email, phone);
        boolean success = studentDAO.addStudent(student);

        System.out.println(success ? "Student added successfully." : "Failed to add student.");
    }

    private static void viewAllStudents() {
        List<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        printHeader();
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void viewStudentById() {
        System.out.print("Enter student ID: ");
        int id = readInt();

        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID " + id);
        } else {
            printHeader();
            System.out.println(student);
        }
    }

    private static void updateStudent() {
        System.out.print("Enter ID of student to update: ");
        int id = readInt();

        Student existing = studentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("No student found with ID " + id);
            return;
        }

        System.out.print("New name (" + existing.getName() + "), Enter to keep: ");
        String name = scanner.nextLine().trim();

        System.out.print("New age (" + existing.getAge() + "), Enter to keep: ");
        String ageInput = scanner.nextLine().trim();

        System.out.print("New course (" + existing.getCourse() + "), Enter to keep: ");
        String course = scanner.nextLine().trim();

        System.out.print("New email (" + existing.getEmail() + "), Enter to keep: ");
        String email = scanner.nextLine().trim();

        System.out.print("New phone (" + existing.getPhone() + "), Enter to keep: ");
        String phone = scanner.nextLine().trim();

        if (!name.isEmpty()) existing.setName(name);
        if (!ageInput.isEmpty()) existing.setAge(Integer.parseInt(ageInput));
        if (!course.isEmpty()) existing.setCourse(course);
        if (!email.isEmpty()) existing.setEmail(email);
        if (!phone.isEmpty()) existing.setPhone(phone);

        boolean success = studentDAO.updateStudent(existing);
        System.out.println(success ? "Student updated successfully." : "Failed to update student.");
    }

    private static void deleteStudent() {
        System.out.print("Enter ID of student to delete: ");
        int id = readInt();

        boolean success = studentDAO.deleteStudent(id);
        System.out.println(success ? "Student deleted successfully." : "No student found with that ID.");
    }

    private static void searchStudents() {
        System.out.print("Enter name keyword: ");
        String keyword = scanner.nextLine().trim();

        List<Student> results = studentDAO.searchStudentsByName(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }

        printHeader();
        for (Student s : results) {
            System.out.println(s);
        }
    }

    private static void printHeader() {
        System.out.printf("%-5s %-20s %-5s %-20s %-25s %-15s%n",
                "ID", "Name", "Age", "Course", "Email", "Phone");
        System.out.println("-".repeat(95));
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
