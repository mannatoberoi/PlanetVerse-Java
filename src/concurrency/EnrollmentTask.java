package concurrency;

import model.Student;
import model.Enrollment;
import service.CourseService;

public class EnrollmentTask implements Runnable {

    private final Student student;
    private final int courseId;
    private final CourseService courseService;
    private final SlotManager slotManager;

    public EnrollmentTask(Student student, int courseId,
                          CourseService courseService,
                          SlotManager slotManager) {

        this.student = student;
        this.courseId = courseId;
        this.courseService = courseService;
        this.slotManager = slotManager;
    }

    @Override
    public void run() {

        if (!slotManager.acquireSlot(courseId)) {
            System.out.println(
                "No seats available for course " + courseId
            );
            return;
        }

        boolean enrollmentCompleted = false;

        try {

            Enrollment enrollment =
                courseService.enrollStudent(student, courseId);

            enrollmentCompleted = true;

            System.out.println(
                "Enrollment successful! ID: "
                + enrollment.getEnrollmentId()
            );

        } catch (RuntimeException e) {

            System.out.println(
                "Enrollment failed: " + e.getMessage()
            );

        } finally {

            if (!enrollmentCompleted) {
                slotManager.releaseSlot(courseId);
            }
        }
    }
}