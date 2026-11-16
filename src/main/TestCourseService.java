
package main;

import service.CourseService;
import dao.StudentDAO;
import model.Student;
import model.Enrollment;

public class TestCourseService {

    public static void main(String[] args) {

        CourseService courseService = new CourseService();
        StudentDAO studentDAO = new StudentDAO();

        // Retrieve existing student
        Student student = studentDAO.getStudentById(9999);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        // Display all courses
        System.out.println("========== ALL COURSES ==========");

        courseService.getAllCourses().forEach(course -> {
            System.out.println(course.displayCourse());
            System.out.println("------------------------");
        });

        // Display all enrollments
        System.out.println("\n========== ALL ENROLLMENTS ==========");

        for (Enrollment enrollment :
                courseService.getAllEnrollments()) {

            System.out.println(
                "Enrollment ID: " + enrollment.getEnrollmentId()
            );

            System.out.println(
                "Student: " + enrollment.getStudent().getName()
            );

            System.out.println(
                "Course: " + enrollment.getCourse().getCourseName()
            );

            System.out.println(
                "Status: " + enrollment.getStatus()
            );

            System.out.println("------------------------");
        }

        // Display student's enrollments
        System.out.println("\n========== STUDENT ENROLLMENTS ==========");

        for (Enrollment enrollment :
                courseService.getStudentEnrollments(student)) {

            System.out.println(
                enrollment.getCourse().getCourseName()
            );
        }

        System.out.println("\nCourseService test completed!");
    }
}
