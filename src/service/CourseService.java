
package service;

import dao.CourseDAO;
import dao.EnrollmentDAO;

import model.Course;
import model.Student;
import model.Enrollment;

import java.util.List;

public class CourseService {

    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    public CourseService() {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    // Add course
    public void addCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException(
                "Course cannot be null"
            );
        }

        if (courseDAO.getCourseById(course.getCourseId()) != null) {
            throw new IllegalArgumentException(
                "Course already exists"
            );
        }

        if (!courseDAO.addCourse(course)) {
            throw new IllegalStateException(
                "Unable to save course in database"
            );
        }
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    // Find course by ID
    public Course findCourseById(int courseId) {
        return courseDAO.getCourseById(courseId);
    }

    // Enroll student
    public synchronized Enrollment enrollStudent(
            Student student, int courseId) {

        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        Course course = findCourseById(courseId);

        if (course == null) {
            throw new IllegalArgumentException(
                "Course not found"
            );
        }

        if (!course.isEligible(student)) {
            throw new IllegalStateException(
                "Student is not eligible for this course"
            );
        }

        if (enrollmentDAO.isAlreadyEnrolled(
                student.getUserId(), courseId)) {

            throw new IllegalStateException(
                "Student is already enrolled"
            );
        }

        int enrolledCount =
            enrollmentDAO.countEnrolledByCourse(courseId);

        if (enrolledCount < 0) {
            throw new IllegalStateException(
                "Unable to check course capacity"
            );
        }

        if (enrolledCount >= course.getCapacity()) {
            throw new IllegalStateException("Course is full");
        }

        int enrollmentId =
            enrollmentDAO.addEnrollmentAuto(student, course);

        if (enrollmentId == -1) {
            throw new IllegalStateException(
                "Unable to save enrollment in database"
            );
        }

        return new Enrollment(enrollmentId, student, course);
    }

    // Get all enrollments from database
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.getAllEnrollments();
    }

    // Get a student's enrollments from database
    public List<Enrollment> getStudentEnrollments(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        return enrollmentDAO.getEnrollmentsByStudent(
            student.getUserId()
        );
    }
}
