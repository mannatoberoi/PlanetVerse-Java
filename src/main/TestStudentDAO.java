
package main;

import dao.StudentDAO;
import model.Student;

public class TestStudentDAO {

    public static void main(String[] args) {

        StudentDAO studentDAO = new StudentDAO();

        // Create a test student
        Student student = new Student(
            9999,
            "Test Student",
            "teststudent@gmail.com",
            "CSE",
            8.5
        );

        // Save student in database
        boolean saved = studentDAO.addStudent(student);

        if (saved) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Student could not be added.");
            return;
        }

        // Retrieve student using ID
        Student retrievedStudent =
            studentDAO.getStudentById(9999);

        if (retrievedStudent != null) {
            System.out.println("\nStudent retrieved successfully!");
            System.out.println(retrievedStudent.displayProfile());
        } else {
            System.out.println("Student not found.");
        }
    }
}
