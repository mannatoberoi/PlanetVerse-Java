package concurrency;

import java.util.concurrent.Callable;

import model.Enrollment;
import model.Student;
import service.CourseService;

public class EnrollmentCallable implements Callable<Enrollment> {

    private final Student student;
    private final int courseId;
    private final CourseService courseService;
    private final SlotManager slotManager;

    public EnrollmentCallable(Student student, int courseId,
                              CourseService courseService,
                              SlotManager slotManager) {

        this.student = student;
        this.courseId = courseId;
        this.courseService = courseService;
        this.slotManager = slotManager;
    }

    @Override
    public Enrollment call() throws Exception {

        if (!slotManager.acquireSlot(courseId)) {
            throw new IllegalStateException(
                "No seats available"
            );
        }

        boolean completed = false;

        try {

            Enrollment enrollment =
                courseService.enrollStudent(student, courseId);

            completed = true;

            return enrollment;

        } finally {

            if (!completed) {
                slotManager.releaseSlot(courseId);
            }
        }
    }
}
