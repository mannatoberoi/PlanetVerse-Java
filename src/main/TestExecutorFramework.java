package main;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class TestExecutorFramework {

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Testing ExecutorService and Future...");

        Callable<String> task1 = () -> {
            return "Task 1 completed";
        };

        Callable<String> task2 = () -> {
            return "Task 2 completed";
        };

        Callable<String> task3 = () -> {
            return "Task 3 completed";
        };

        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);
        Future<String> future3 = executor.submit(task3);

        System.out.println(future1.get());
        System.out.println(future2.get());
        System.out.println(future3.get());

        executor.shutdown();

        System.out.println();
        System.out.println("PASS: ExecutorService and Future test successful!");
    }
}