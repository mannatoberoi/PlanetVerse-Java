
package concurrency;

public class EnrollmentConsumer implements Runnable {

    private final EnrollmentRequestQueue queue;

    public EnrollmentConsumer(EnrollmentRequestQueue queue) {

        if (queue == null) {
            throw new IllegalArgumentException(
                "Queue cannot be null"
            );
        }

        this.queue = queue;
    }

    @Override
    public void run() {

        try {

            EnrollmentTask task;

            while ((task = queue.take()) != null) {
                task.run();
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}