
package concurrency;

import java.util.concurrent.BlockingQueue;

public class EnrollmentProducer {

    private BlockingQueue<EnrollmentTask> queue;

    public EnrollmentProducer(BlockingQueue<EnrollmentTask> queue) {
        this.queue = queue;
    }

    public void produce(EnrollmentTask task)
            throws InterruptedException {

        queue.put(task);

        System.out.println("Enrollment request added to queue.");
    }
}
