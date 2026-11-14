
package model;

import java.time.LocalDateTime;

public class Enrollment {

    private int enrollmentId;
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;

    public Enrollment(int enrollmentId, Student student, Course course) {

        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ENROLLED;
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
