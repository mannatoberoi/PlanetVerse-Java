
package dao;

import database.DBConnection;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // Delete student by ID
public boolean deleteStudent(int userId) {

    String sql = "DELETE FROM students WHERE user_id = ?";

    try (Connection con = DBConnection.getConnection()) {

        if (con == null) return false;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println(e.getMessage());
        return false;
    }
}

    // Insert student
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students "
                   + "(user_id, name, email, department, cgpa) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, student.getUserId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getDepartment());
            ps.setDouble(5, student.getCgpa());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Get student by ID
    public Student getStudentById(int userId) {

        String sql = "SELECT * FROM students WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return null;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Student(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getDouble("cgpa")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Get all students
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return students;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                students.add(new Student(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getDouble("cgpa")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return students;
    }
}
