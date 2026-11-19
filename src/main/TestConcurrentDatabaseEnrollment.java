
package main;

import dao.TransactionalEnrollmentDAO;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestConcurrentDatabaseEnrollment {

    private static final int COURSE_ID = 6001;
    private static final int CAPACITY = 2;
    private static final int NUMBER_OF_STUDENTS = 5;
    private static final int FIRST_STUDENT_ID = 9601;

    public static void main(String[] args) throws Exception {

        setupTestData();

        ExecutorService executor =
                Executors.newFixedThreadPool(NUMBER_OF_STUDENTS);

        CountDownLatch ready =
                new CountDownLatch(NUMBER_OF_STUDENTS);

        CountDownLatch start =
                new CountDownLatch(1);

        List<Future<Integer>> results = new ArrayList<>();

        try {
            System.out.println("Starting concurrent enrollment test...");
            System.out.println("Course capacity: " + CAPACITY);
            System.out.println("Students requesting seats: "
                    + NUMBER_OF_STUDENTS);

            for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {

                final int studentId = FIRST_STUDENT_ID + i;

                results.add(executor.submit(() -> {

                    ready.countDown();
                    start.await();

                    TransactionalEnrollmentDAO dao =
                            new TransactionalEnrollmentDAO();

                    return dao.enroll(studentId, COURSE_ID);
                }));
            }

            // Wait until all five threads are ready.
            ready.await();

            // Release all threads together.
            start.countDown();

            int successfulEnrollments = 0;

            for (Future<Integer> result : results) {

                int enrollmentId = result.get();

                if (enrollmentId > 0) {
                    successfulEnrollments++;

                    System.out.println(
                            "Enrollment successful. ID: " + enrollmentId
                    );
                } else {
                    System.out.println(
                            "Enrollment rejected. No seat available."
                    );
                }
            }

            System.out.println("\nSuccessful enrollments: "
                    + successfulEnrollments);

            if (successfulEnrollments == CAPACITY) {
                System.out.println(
                        "PASS: Database capacity protection works!"
                );
            } else {
                System.out.println(
                        "FAIL: Expected exactly " + CAPACITY
                                + " successful enrollments."
                );
            }

        } finally {
            executor.shutdown();

            // Remove test records so the test can be run again.
            cleanupTestData();
        }
    }

    private static void setupTestData() throws SQLException {

        try (Connection conn = DBConnection.getConnection()) {

            // Remove records from an earlier test run.
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM enrollments WHERE course_id = ?")) {
                ps.setInt(1, COURSE_ID);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM students WHERE user_id BETWEEN ? AND ?")) {
                ps.setInt(1, FIRST_STUDENT_ID);
                ps.setInt(2, FIRST_STUDENT_ID + NUMBER_OF_STUDENTS - 1);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM courses WHERE course_id = ?")) {
                ps.setInt(1, COURSE_ID);
                ps.executeUpdate();
            }

            // Create a course with capacity of two.
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO courses " +
                    "(course_id, course_name, minimum_cgpa, capacity) " +
                    "VALUES (?, ?, ?, ?)")) {

                ps.setInt(1, COURSE_ID);
                ps.setString(2, "Concurrent Test Course");
                ps.setDouble(3, 7.0);
                ps.setInt(4, CAPACITY);

                ps.executeUpdate();
            }

            // Create five eligible students.
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO students " +
                    "(user_id, name, email, department, cgpa) " +
                    "VALUES (?, ?, ?, ?, ?)")) {

                for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {

                    int studentId = FIRST_STUDENT_ID + i;

                    ps.setInt(1, studentId);
                    ps.setString(2, "Concurrent Student " + (i + 1));
                    ps.setString(3,
                            "concurrent" + studentId + "@test.com");
                    ps.setString(4, "CSE");
                    ps.setDouble(5, 8.5);

                    ps.executeUpdate();
                }
            }
        }
    }

    private static void cleanupTestData() throws SQLException {

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM enrollments WHERE course_id = ?")) {
                ps.setInt(1, COURSE_ID);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM students WHERE user_id BETWEEN ? AND ?")) {
                ps.setInt(1, FIRST_STUDENT_ID);
                ps.setInt(2, FIRST_STUDENT_ID + NUMBER_OF_STUDENTS - 1);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM courses WHERE course_id = ?")) {
                ps.setInt(1, COURSE_ID);
                ps.executeUpdate();
            }
        }
    }
}
