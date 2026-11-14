
package service;

import model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private List<Student> students;

    public StudentService() {
        students = new ArrayList<>();
    }

    // Add student
    public void addStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        if (findStudentById(student.getUserId()) != null) {
            throw new IllegalArgumentException(
                "Student already exists"
            );
        }

        students.add(student);
    }

    // Find student by ID
    public Student findStudentById(int userId) {

        for (Student student : students) {

            if (student.getUserId() == userId) {
                return student;
            }
        }

        return null;
    }

    // Get all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // Remove student
    public boolean removeStudent(int userId) {

        Student student = findStudentById(userId);

        if (student == null) {
            return false;
        }

        students.remove(student);
        return true;
    }
}
