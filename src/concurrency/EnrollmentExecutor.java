package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import model.Enrollment;
import model.Student;
import service.CourseService;

public class EnrollmentExecutor {

    private final ExecutorService executorService;
    private final CourseService courseService;
    private final SlotManager slotManager;

    public EnrollmentExecutor(int numberOfThreads,
                               CourseService courseService,
                               SlotManager slotManager) {

        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException(
                "Number of threads must be greater than zero"
            );
        }

        this.executorService =
            Executors.newFixedThreadPool(numberOfThreads);

        this.courseService = courseService;
        this.slotManager = slotManager;
    }

    // Submit enrollment and receive Future
    public Future<Enrollment> submitEnrollment(
            Student student, int courseId) {

        return executorService.submit(
            new EnrollmentCallable(
                student,
                courseId,
                courseService,
                slotManager
            )
        );
    }

    // Stop executor
    public void shutdown() {
        executorService.shutdown();
    }
}