package concurrency;

import java.util.ArrayDeque;
import java.util.Queue;

public class EnrollmentRequestQueue {

    private final Queue<EnrollmentTask> tasks =
            new ArrayDeque<>();

    private boolean closed = false;

    // Add request to queue
    public synchronized void put(EnrollmentTask task)
            throws InterruptedException {

        if (task == null) {
            throw new IllegalArgumentException(
                "Task cannot be null"
            );
        }

        if (closed) {
            throw new IllegalStateException(
                "Queue is closed"
            );
        }

        tasks.add(task);

        notifyAll();
    }

    // Retrieve request from queue
    public synchronized EnrollmentTask take()
            throws InterruptedException {

        while (tasks.isEmpty() && !closed) {
            wait();
        }

        if (tasks.isEmpty() && closed) {
            return null;
        }

        return tasks.remove();
    }

    // Close queue
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public synchronized int size() {
        return tasks.size();
    }
}