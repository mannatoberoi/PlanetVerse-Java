package main;

import dao.StudentDAO;
import model.Student;
import model.Course;
import model.Enrollment;
import service.CourseService;

import java.util.List;
import java.util.Scanner;

public class PlanetVerseApp {

    private static final Scanner scanner = new Scanner(System.in);

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseService courseService = new CourseService();

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       PLANETVERSE JAVA");
        System.out.println("  Student Course Enrollment");
        System.out.println("================================");

        while (true) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {

                    case 1:
                        registerStudent();
                        break;

                    case 2:
                        viewStudents();
                        break;

                    case 3:
                        addCourse();
                        break;

                    case 4:
                        viewCourses();
                        break;

                    case 5:
                        enrollStudent();
                        break;

                    case 6:
                        viewAllEnrollments();
                        break;

                    case 7:
                        viewStudentEnrollments();
                        break;

                    case 8:
                        System.out.println("Exiting PlanetVerse...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void displayMenu() {

        System.out.println("\n========== MENU ==========");
        System.out.println("1. Register Student");
        System.out.println("2. View Students");
        System.out.println("3. Add Course");
        System.out.println("4. View Courses");
        System.out.println("5. Enroll Student");
        System.out.println("6. View All Enrollments");
        System.out.println("7. View Student Enrollments");
        System.out.println("8. Exit");
        System.out.println("=========================");
    }

    // Register a student
    private static void registerStudent() {

        int id = readInt("Enter student ID: ");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter department: ");
        String department = scanner.nextLine();

        double cgpa = readDouble("Enter CGPA: ");

        Student student = new Student(
                id, name, email, department, cgpa
        );

        if (studentDAO.addStudent(student)) {
            System.out.println("Student registered successfully!");
        } else {
            System.out.println("Student registration failed.");
        }
    }

    // View students
    private static void viewStudents() {

        List<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n========== STUDENTS ==========");

        for (Student student : students) {

            System.out.println(
                    "ID: " + student.getUserId()
                    + " | Name: " + student.getName()
                    + " | Email: " + student.getEmail()
                    + " | Department: " + student.getDepartment()
                    + " | CGPA: " + student.getCgpa()
            );
        }
    }

    // Add course
    private static void addCourse() {

        int id = readInt("Enter course ID: ");

        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        double minimumCgpa =
                readDouble("Enter minimum CGPA: ");

        int capacity = readInt("Enter course capacity: ");

        Course course = new Course(
                id, name, minimumCgpa, capacity
        );

        courseService.addCourse(course);

        System.out.println("Course added successfully!");
    }

    // View courses
    private static void viewCourses() {

        List<Course> courses =
                courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n========== COURSES ==========");

        for (Course course : courses) {

            System.out.println(
                    "ID: " + course.getCourseId()
                    + " | Name: " + course.getCourseName()
                    + " | Minimum CGPA: " + course.getMinimumCgpa()
                    + " | Capacity: " + course.getCapacity()
            );
        }
    }

    // Enroll student
    private static void enrollStudent() {

        int studentId = readInt("Enter student ID: ");

        int courseId = readInt("Enter course ID: ");

        Student student =
                studentDAO.getStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        Enrollment enrollment =
                courseService.enrollStudent(student, courseId);

        System.out.println(
                "Enrollment successful! Enrollment ID: "
                + enrollment.getEnrollmentId()
        );
    }

    // View all enrollments
    private static void viewAllEnrollments() {

        List<Enrollment> enrollments =
                courseService.getAllEnrollments();

        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        System.out.println("\n========== ENROLLMENTS ==========");

        for (Enrollment enrollment : enrollments) {

            System.out.println(
                    "Enrollment ID: "
                    + enrollment.getEnrollmentId()
                    + " | Student: "
                    + enrollment.getStudent().getName()
                    + " | Course: "
                    + enrollment.getCourse().getCourseName()
                    + " | Status: "
                    + enrollment.getStatus()
            );
        }
    }

    // View enrollments of one student
    private static void viewStudentEnrollments() {

        int studentId = readInt("Enter student ID: ");

        Student student =
                studentDAO.getStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        List<Enrollment> enrollments =
                courseService.getStudentEnrollments(student);

        if (enrollments.isEmpty()) {
            System.out.println("This student has no enrollments.");
            return;
        }

        System.out.println(
                "\nEnrollments for " + student.getName()
        );

        for (Enrollment enrollment : enrollments) {

            System.out.println(
                    "Enrollment ID: "
                    + enrollment.getEnrollmentId()
                    + " | Course: "
                    + enrollment.getCourse().getCourseName()
                    + " | Status: "
                    + enrollment.getStatus()
            );
        }
    }

    // Read integer safely
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                int value = Integer.parseInt(
                        scanner.nextLine()
                );

                return value;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid integer."
                );
            }
        }
    }

    // Read double safely
    private static double readDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                double value = Double.parseDouble(
                        scanner.nextLine()
                );

                return value;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}