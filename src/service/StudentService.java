
package service;

import dao.StudentDAO;
import model.Student;

import java.util.List;

public class StudentService {

    private StudentDAO studentDAO;

    public StudentService() {
        studentDAO = new StudentDAO();
    }

    // Add student
    public void addStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        if (studentDAO.getStudentById(student.getUserId()) != null) {
            throw new IllegalArgumentException(
                "Student already exists"
            );
        }

        if (!studentDAO.addStudent(student)) {
            throw new IllegalStateException(
                "Unable to save student in database"
            );
        }
    }

    // Find student by ID
    public Student findStudentById(int userId) {
        return studentDAO.getStudentById(userId);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // Remove student
    public boolean removeStudent(int userId) {
        return studentDAO.deleteStudent(userId);
    }
}
