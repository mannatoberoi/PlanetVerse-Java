
package main;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.StudentDAO;

import model.Course;
import model.Student;

public class TestCourseEnrollmentDAO {

    public static void main(String[] args) {

        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        // Step 1: Retrieve existing student
        Student student = studentDAO.getStudentById(9999);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Student retrieved successfully!");

        // Step 2: Retrieve existing course
        Course course = courseDAO.getCourseById(5001);

        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        System.out.println("Course retrieved successfully!");

        // Step 3: Check duplicate enrollment
        if (enrollmentDAO.isAlreadyEnrolled(
                student.getUserId(), course.getCourseId())) {

            System.out.println(
                "Student is already enrolled in this course!"
            );

            return;
        }

        // Step 4: Check course capacity
        int enrolledCount =
            enrollmentDAO.countEnrolledByCourse(course.getCourseId());

        if (enrolledCount < 0) {
            System.out.println("Unable to check course capacity!");
            return;
        }

        if (enrolledCount >= course.getCapacity()) {
            System.out.println("Course is full!");
            return;
        }

        // Step 5: Save enrollment directly in database
        int enrollmentId = enrollmentDAO.addEnrollmentAuto(
            student, course
        );

        if (enrollmentId != -1) {

            System.out.println(
                "\nEnrollment saved successfully!"
            );

            System.out.println(
                "Enrollment ID: " + enrollmentId
            );

        } else {
            System.out.println(
                "Enrollment could not be saved."
            );
        }

        // Step 6: Display all enrollments from database
        System.out.println("\nAll Enrollments:");

        enrollmentDAO.getAllEnrollments().forEach(enrollment -> {

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
        });
    }
}
