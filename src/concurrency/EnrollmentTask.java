
package concurrency;

import model.Student;
import service.CourseService;
import model.Enrollment;

public class EnrollmentTask implements Runnable {

    private Student student;
    private int courseId;
    private CourseService courseService;
    private SlotManager slotManager;

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

        boolean slotAcquired = slotManager.acquireSlot(courseId);

        if (!slotAcquired) {
            System.out.println("No seats available!");
            return;
        }

        try {
            Enrollment enrollment =
                courseService.enrollStudent(student, courseId);

            System.out.println(
                "Enrollment successful! ID: "
                + enrollment.getEnrollmentId()
            );

        } catch (RuntimeException e) {

            slotManager.releaseSlot(courseId);

            System.out.println(
                "Enrollment failed: " + e.getMessage()
            );
        }
    }
}
