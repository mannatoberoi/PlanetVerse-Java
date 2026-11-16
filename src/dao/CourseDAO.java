
package dao;

import database.DBConnection;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Insert course
    public boolean addCourse(Course course) {

        String sql = "INSERT INTO courses "
                   + "(course_id, course_name, minimum_cgpa, capacity) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, course.getCourseId());
            ps.setString(2, course.getCourseName());
            ps.setDouble(3, course.getMinimumCgpa());
            ps.setInt(4, course.getCapacity());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Get course by ID
    public Course getCourseById(int courseId) {

        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return null;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getDouble("minimum_cgpa"),
                    rs.getInt("capacity")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Get all courses
    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM courses";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return courses;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                courses.add(new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getDouble("minimum_cgpa"),
                    rs.getInt("capacity")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return courses;
    }
}
