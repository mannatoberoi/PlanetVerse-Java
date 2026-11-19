
package service;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.TransactionalEnrollmentDAO;

import model.Course;
import model.Student;
import model.Enrollment;

import java.util.List;

public class CourseService {

    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private TransactionalEnrollmentDAO transactionalEnrollmentDAO;

    public CourseService() {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        transactionalEnrollmentDAO = new TransactionalEnrollmentDAO();
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

    // Enroll student using database transaction
    public Enrollment enrollStudent(Student student, int courseId) {

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

        int enrollmentId = transactionalEnrollmentDAO.enroll(
            student.getUserId(),
            courseId
        );

        if (enrollmentId == -1) {
            throw new IllegalStateException(
                "Enrollment rejected: student may be ineligible, "
                + "already enrolled, or the course may be full."
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
