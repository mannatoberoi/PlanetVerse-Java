
package dao;

import database.DBConnection;
import model.Enrollment;
import model.EnrollmentStatus;
import model.Student;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // Add enrollment with automatic ID
    public int addEnrollmentAuto(Student student, Course course) {

        String sql = "INSERT INTO enrollments "
                   + "(student_id, course_id, enrollment_date, status) "
                   + "VALUES (?, ?, NOW(), ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return -1;

            PreparedStatement ps = con.prepareStatement(
                sql, PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, student.getUserId());
            ps.setInt(2, course.getCourseId());
            ps.setString(3, "ENROLLED");

            if (ps.executeUpdate() > 0) {

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    // Check duplicate enrollment
    public boolean isAlreadyEnrolled(int studentId, int courseId) {

        String sql = "SELECT COUNT(*) FROM enrollments "
                   + "WHERE student_id = ? AND course_id = ? "
                   + "AND status = 'ENROLLED'";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Count enrolled students
    public int countEnrolledByCourse(int courseId) {

        String sql = "SELECT COUNT(*) FROM enrollments "
                   + "WHERE course_id = ? AND status = 'ENROLLED'";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return -1;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {

        List<Enrollment> enrollments = new ArrayList<>();

        String sql = "SELECT e.*, "
                   + "s.name, s.email, s.department, s.cgpa, "
                   + "c.course_name, c.minimum_cgpa, c.capacity "
                   + "FROM enrollments e "
                   + "JOIN students s ON e.student_id = s.user_id "
                   + "JOIN courses c ON e.course_id = c.course_id";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return enrollments;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                enrollments.add(mapEnrollment(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return enrollments;
    }

    // Get enrollments of a particular student
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {

        List<Enrollment> enrollments = new ArrayList<>();

        String sql = "SELECT e.*, "
                   + "s.name, s.email, s.department, s.cgpa, "
                   + "c.course_name, c.minimum_cgpa, c.capacity "
                   + "FROM enrollments e "
                   + "JOIN students s ON e.student_id = s.user_id "
                   + "JOIN courses c ON e.course_id = c.course_id "
                   + "WHERE e.student_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return enrollments;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                enrollments.add(mapEnrollment(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return enrollments;
    }

    // Convert database record into Enrollment object
    private Enrollment mapEnrollment(ResultSet rs)
            throws SQLException {

        Student student = new Student(
            rs.getInt("student_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("department"),
            rs.getDouble("cgpa")
        );

        Course course = new Course(
            rs.getInt("course_id"),
            rs.getString("course_name"),
            rs.getDouble("minimum_cgpa"),
            rs.getInt("capacity")
        );

        Timestamp timestamp = rs.getTimestamp("enrollment_date");

        return new Enrollment(
            rs.getInt("enrollment_id"),
            student,
            course,
            timestamp.toLocalDateTime(),
            EnrollmentStatus.valueOf(rs.getString("status"))
        );
    }
}
