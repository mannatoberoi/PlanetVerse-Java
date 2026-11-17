package concurrency;

public class EnrollmentProducer {

    private final EnrollmentRequestQueue queue;

    public EnrollmentProducer(EnrollmentRequestQueue queue) {

        if (queue == null) {
            throw new IllegalArgumentException(
                "Queue cannot be null"
            );
        }

        this.queue = queue;
    }

    public void produce(EnrollmentTask task)
            throws InterruptedException {

        queue.put(task);

        System.out.println(
            "Enrollment request added to queue."
        );
    }
}
