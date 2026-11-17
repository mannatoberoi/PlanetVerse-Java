package main;

import concurrency.SlotManager;
import model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestConcurrency {

    public static void main(String[] args) throws Exception {

        Course course = new Course(
            9901,
            "Concurrency Test Course",
            0.0,
            2
        );

        SlotManager slotManager = new SlotManager();

        slotManager.registerCourse(course, 0);

        int numberOfStudents = 5;

        CountDownLatch startSignal =
            new CountDownLatch(1);

        ExecutorService executor =
            Executors.newFixedThreadPool(numberOfStudents);

        List<Future<Boolean>> results =
            new ArrayList<>();

        // Create five simultaneous requests
        for (int i = 0; i < numberOfStudents; i++) {

            results.add(executor.submit(() -> {

                startSignal.await();

                return slotManager.acquireSlot(9901);
            }));
        }

        // Release all requests together
        startSignal.countDown();

        int successfulRequests = 0;

        for (Future<Boolean> result : results) {

            if (result.get()) {
                successfulRequests++;
            }
        }

        executor.shutdown();

        System.out.println(
            "Students requesting seats: " + numberOfStudents
        );

        System.out.println(
            "Course capacity: " + course.getCapacity()
        );

        System.out.println(
            "Successful seat acquisitions: "
            + successfulRequests
        );

        System.out.println(
            "Remaining seats: "
            + slotManager.getAvailableSlots(9901)
        );

        if (successfulRequests == 2) {
            System.out.println("Semaphore test passed!");
        } else {
            System.out.println("Semaphore test failed!");
        }
    }
}