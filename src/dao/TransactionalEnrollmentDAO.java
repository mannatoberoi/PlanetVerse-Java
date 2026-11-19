
package dao;

import database.DBConnection;
import exception.EnrollmentException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionalEnrollmentDAO {

    public int enroll(int studentId, int courseId) {

        if (studentId <= 0 || courseId <= 0) {
            return -1;
        }

        try (Connection conn = DBConnection.getConnection()) {

            conn.setTransactionIsolation(
                Connection.TRANSACTION_READ_COMMITTED
            );
            conn.setAutoCommit(false);

            try {
                // 1. Lock the course row
                double minimumCgpa;
                int capacity;

                String courseSql =
                    "SELECT minimum_cgpa, capacity " +
                    "FROM courses WHERE course_id = ? FOR UPDATE";

                try (PreparedStatement ps =
                         conn.prepareStatement(courseSql)) {

                    ps.setInt(1, courseId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return -1;
                        }

                        minimumCgpa = rs.getDouble("minimum_cgpa");
                        capacity = rs.getInt("capacity");
                    }
                }

                // 2. Check student eligibility
                String studentSql =
                    "SELECT cgpa FROM students WHERE user_id = ?";

                try (PreparedStatement ps =
                         conn.prepareStatement(studentSql)) {

                    ps.setInt(1, studentId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() ||
                            rs.getDouble("cgpa") < minimumCgpa) {

                            conn.rollback();
                            return -1;
                        }
                    }
                }

                // 3. Prevent duplicate enrollment
                String duplicateSql =
                    "SELECT COUNT(*) FROM enrollments " +
                    "WHERE student_id = ? AND course_id = ?";

                try (PreparedStatement ps =
                         conn.prepareStatement(duplicateSql)) {

                    ps.setInt(1, studentId);
                    ps.setInt(2, courseId);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();

                        if (rs.getInt(1) > 0) {
                            conn.rollback();
                            return -1;
                        }
                    }
                }

                // 4. Check remaining capacity
                String countSql =
                    "SELECT COUNT(*) FROM enrollments " +
                    "WHERE course_id = ? AND status = 'ENROLLED'";

                try (PreparedStatement ps =
                         conn.prepareStatement(countSql)) {

                    ps.setInt(1, courseId);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();

                        if (rs.getInt(1) >= capacity) {
                            conn.rollback();
                            return -1;
                        }
                    }
                }

                // 5. Insert enrollment
                String insertSql =
                    "INSERT INTO enrollments " +
                    "(student_id, course_id, enrollment_date, status) " +
                    "VALUES (?, ?, NOW(), 'ENROLLED')";

                int enrollmentId;

                try (PreparedStatement ps =
                         conn.prepareStatement(
                             insertSql,
                             Statement.RETURN_GENERATED_KEYS
                         )) {

                    ps.setInt(1, studentId);
                    ps.setInt(2, courseId);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                "Enrollment ID was not generated"
                            );
                        }

                        enrollmentId = keys.getInt(1);
                    }
                }

                // 6. Save all changes
                conn.commit();
                return enrollmentId;

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw new EnrollmentException(
                    "Enrollment transaction failed", e
                );
            }

        } catch (SQLException e) {
            throw new EnrollmentException(
                "Database connection failed", e
            );
        }
    }
}
