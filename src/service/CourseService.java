
package service;

import model.Course;
import model.Student;
import model.Enrollment;
import model.EnrollmentStatus;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private List<Course> courses;
    private List<Enrollment> enrollments;
    private int nextEnrollmentId;

    public CourseService() {
        courses = new ArrayList<>();
        enrollments = new ArrayList<>();
        nextEnrollmentId = 1;
    }

    // Add course
    public void addCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        courses.add(course);
    }

    // View all courses
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    // Find course by ID
    public Course findCourseById(int courseId) {

        for (Course course : courses) {

            if (course.getCourseId() == courseId) {
                return course;
            }
        }

        return null;
    }

    // Enroll student
    public synchronized Enrollment enrollStudent(
            Student student, int courseId) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        Course course = findCourseById(courseId);

        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        if (!course.isEligible(student)) {
            throw new IllegalStateException(
                "Student is not eligible for this course"
            );
        }

        long enrolledCount = enrollments.stream()
                .filter(e -> e.getCourse().getCourseId() == courseId)
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .count();

        if (enrolledCount >= course.getCapacity()) {
            throw new IllegalStateException("Course is full");
        }

        for (Enrollment enrollment : enrollments) {

            if (enrollment.getStudent() == student &&
                enrollment.getCourse().getCourseId() == courseId &&
                enrollment.getStatus() == EnrollmentStatus.ENROLLED) {

                throw new IllegalStateException(
                    "Student is already enrolled"
                );
            }
        }

        Enrollment enrollment = new Enrollment(
            nextEnrollmentId++, student, course
        );

        enrollments.add(enrollment);

        return enrollment;
    }

    // View all enrollments
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }

    // View student's enrollments
    public List<Enrollment> getStudentEnrollments(Student student) {

        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {

            if (enrollment.getStudent() == student) {
                result.add(enrollment);
            }
        }

        return result;
    }
}
