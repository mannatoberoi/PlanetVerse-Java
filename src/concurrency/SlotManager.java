package concurrency;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import model.Course;

public class SlotManager {
    private ConcurrentHashMap<Integer, Semaphore> courseSlots;
    public SlotManager() {
        courseSlots = new ConcurrentHashMap<>();
    }
    // Register course capacity
    public void registerCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        courseSlots.put(
            course.getCourseId(),
            new Semaphore(course.getCapacity())
        );
    }

    // Acquire a slot
    public boolean acquireSlot(int courseId) {
        Semaphore semaphore = courseSlots.get(courseId);
        if (semaphore == null) {
            return false;
        }
        return semaphore.tryAcquire();
    }

    // Release a slot
    public void releaseSlot(int courseId) {
        Semaphore semaphore = courseSlots.get(courseId);
        if (semaphore != null) {
            semaphore.release();
        }
    }

    // Check available slots
    public int getAvailableSlots(int courseId) {

        Semaphore semaphore = courseSlots.get(courseId);

        if (semaphore == null) {
            return 0;
        }

        return semaphore.availablePermits();
    }
}
