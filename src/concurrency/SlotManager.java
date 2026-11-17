package concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import model.Course;

public class SlotManager {

    private final ConcurrentHashMap<Integer, Semaphore> courseSlots =
            new ConcurrentHashMap<>();

    // Register course
    public void registerCourse(Course course) {
        registerCourse(course, 0);
    }

    // Register course with already occupied seats
    public void registerCourse(Course course, int alreadyEnrolled) {

        if (course == null) {
            throw new IllegalArgumentException(
                "Course cannot be null"
            );
        }

        if (alreadyEnrolled < 0) {
            throw new IllegalArgumentException(
                "Already enrolled count cannot be negative"
            );
        }

        int available = Math.max(
            0, course.getCapacity() - alreadyEnrolled
        );

        courseSlots.put(
            course.getCourseId(),
            new Semaphore(available, true)
        );
    }

    // Acquire a seat
    public boolean acquireSlot(int courseId) {

        Semaphore semaphore = courseSlots.get(courseId);

        return semaphore != null && semaphore.tryAcquire();
    }

    // Release a seat
    public void releaseSlot(int courseId) {

        Semaphore semaphore = courseSlots.get(courseId);

        if (semaphore != null) {
            semaphore.release();
        }
    }

    // Get remaining seats
    public int getAvailableSlots(int courseId) {

        Semaphore semaphore = courseSlots.get(courseId);

        return semaphore == null
                ? 0
                : semaphore.availablePermits();
    }
}