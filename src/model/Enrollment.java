
package model;

import java.time.LocalDateTime;

public class Enrollment {

    private int enrollmentId;
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;

    // Three-argument constructor
    public Enrollment(int enrollmentId, Student student, Course course) {

        this(
            enrollmentId,
            student,
            course,
            LocalDateTime.now(),
            EnrollmentStatus.ENROLLED
        );
    }

    // Five-argument constructor
    public Enrollment(
            int enrollmentId,
            Student student,
            Course course,
            LocalDateTime enrollmentDate,
            EnrollmentStatus status) {

        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
