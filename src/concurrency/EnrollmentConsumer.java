
package concurrency;

import java.util.concurrent.BlockingQueue;

public class EnrollmentConsumer implements Runnable {

    private BlockingQueue<EnrollmentTask> queue;

    public EnrollmentConsumer(BlockingQueue<EnrollmentTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            try {
                EnrollmentTask task = queue.take();

                task.run();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }
}

