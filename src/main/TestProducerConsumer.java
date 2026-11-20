
package main;

import concurrency.EnrollmentConsumer;
import concurrency.EnrollmentProducer;
import concurrency.EnrollmentRequestQueue;
import concurrency.EnrollmentTask;
import concurrency.SlotManager;

import database.DBConnection;
import model.Course;
import model.Student;
import service.CourseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestProducerConsumer {

    private static final int COURSE_ID = 6101;
    private static final int FIRST_STUDENT_ID = 9701;
    private static final int NUMBER_OF_STUDENTS = 5;

    public static void main(String[] args) throws Exception {

        setupTestData();

        EnrollmentRequestQueue queue =
                new EnrollmentRequestQueue();

        EnrollmentProducer producer =
                new EnrollmentProducer(queue);

        EnrollmentConsumer consumer =
                new EnrollmentConsumer(queue);

        Course course = new Course(
                COURSE_ID,
                "Producer Consumer Test",
                7.0,
                NUMBER_OF_STUDENTS
        );

        SlotManager slotManager = new SlotManager();
        slotManager.registerCourse(course, 0);

        CourseService courseService = new CourseService();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        try {
            System.out.println("PRODUCER-CONSUMER TEST");
            System.out.println("----------------------");

            // Start the consumer in a separate thread.
            Future<?> consumerFuture =
                    executor.submit(consumer);

            // Produce five enrollment requests.
            for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {

                int studentId = FIRST_STUDENT_ID + i;

                Student student = new Student(
                        studentId,
                        "Queue Student " + (i + 1),
                        "queue" + studentId + "@test.com",
                        "CSE",
                        8.5
                );

                EnrollmentTask task = new EnrollmentTask(
                        student,
                        COURSE_ID,
                        courseService,
                        slotManager
                );

                producer.produce(task);

                System.out.println(
                        "Producer added request for student "
                                + studentId
                );
            }

            // Tell the consumer no more requests are coming.
            queue.close();

            // Wait until the consumer finishes processing.
            consumerFuture.get();

            int enrolledCount = getEnrollmentCount();

            System.out.println("\nRESULTS");
            System.out.println("----------------------");
            System.out.println(
                    "Requests produced: " + NUMBER_OF_STUDENTS
            );
            System.out.println(
                    "Requests processed: " + NUMBER_OF_STUDENTS
            );
            System.out.println(
                    "Successful database enrollments: " + enrolledCount
            );

            if (enrolledCount == NUMBER_OF_STUDENTS
                    && queue.size() == 0) {

                System.out.println(
                        "PASS: Producer-consumer test successful!"
                );

            } else {

                System.out.println(
                        "FAIL: Producer-consumer test did not complete correctly."
                );
            }

        } finally {
            queue.close();
            executor.shutdown();
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
                ps.setInt(2,
                        FIRST_STUDENT_ID + NUMBER_OF_STUDENTS - 1);

                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM courses WHERE course_id = ?")) {

                ps.setInt(1, COURSE_ID);
                ps.executeUpdate();
            }

            // Create test course.
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO courses "
                    + "(course_id, course_name, minimum_cgpa, capacity) "
                    + "VALUES (?, ?, ?, ?)")) {

                ps.setInt(1, COURSE_ID);
                ps.setString(2, "Producer Consumer Test");
                ps.setDouble(3, 7.0);
                ps.setInt(4, NUMBER_OF_STUDENTS);

                ps.executeUpdate();
            }

            // Create five eligible students.
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO students "
                    + "(user_id, name, email, department, cgpa) "
                    + "VALUES (?, ?, ?, ?, ?)")) {

                for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {

                    int studentId = FIRST_STUDENT_ID + i;

                    ps.setInt(1, studentId);
                    ps.setString(2, "Queue Student " + (i + 1));
                    ps.setString(3,
                            "queue" + studentId + "@test.com");
                    ps.setString(4, "CSE");
                    ps.setDouble(5, 8.5);

                    ps.executeUpdate();
                }
            }
        }
    }

    private static int getEnrollmentCount() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM enrollments "
                + "WHERE course_id = ? AND status = 'ENROLLED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, COURSE_ID);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
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
                ps.setInt(2,
                        FIRST_STUDENT_ID + NUMBER_OF_STUDENTS - 1);

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
