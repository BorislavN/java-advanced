package streams_files_dirs.sandbox;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadingExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        String input = "Task1\nTask2\nTask3\nTask4\nTask5\nTask6\nTask7\nTask8\nTask9\nTask10";

        singleThreadExample(input);
        multiThreadExample(input);
    }

    private static void singleThreadExample(String input) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));

        String line;
        Instant start = Instant.now();

        while (null != (line = reader.readLine())) {
            Thread.sleep(500);//Simulating some work
            System.out.println(line + ": COMPLETED!");
        }

        Instant end = Instant.now();

        System.out.printf("%nSingleThreadExample: finished in %dms%n%n", Duration.between(start, end).toMillis());
    }

    private static void multiThreadExample(String input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        String line;
        Instant start = Instant.now();

        while (null != (line = reader.readLine())) {
            executorService.submit(lineHandler(line));
        }

        shutdownAndAwaitTermination(executorService);

        Instant end = Instant.now();

        System.out.printf("%nMultiThreadExample: finished in %dms%n%n", Duration.between(start, end).toMillis());
    }

    private static Runnable lineHandler(String line) {
        return () -> {
            try {
                Thread.sleep(500);//Simulating some work
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(line + ": COMPLETED!");
        };
    }

    //This example is directly taken from the ExecutorService official documentation
    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted

        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}