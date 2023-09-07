package streams_files_dirs.sandbox;

import java.io.IOException;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

//The executor service allows us to catch the exceptions in the main thread
public class ExecutorServiceExceptionDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);

        try {
            Future<?> future1 = service.submit(createRunnable(1000));
            Future<String> future2 = service.submit(createCallable(2000));

            future1.get();
            future2.get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Exception caught in catch block!");
            System.out.println(e.getMessage());
        }

        shutdownAwaitTermination(service);
    }

    public static Runnable createRunnable(int timeout) {
        return () -> {
            try {
                Thread.sleep(timeout);
                throw new RuntimeException(Thread.currentThread().getName() + " Runnable: Oops...");
            } catch (InterruptedException e) {
                throw new RuntimeException(Thread.currentThread().getName() + " was interrupted!");
            }
        };
    }

    public static Callable<String> createCallable(int timeout) {
        return () -> {
            try {
                Thread.sleep(timeout);
                throw new IOException(Thread.currentThread().getName() + " Callable:  Oops...");
            } catch (InterruptedException e) {
                throw new RuntimeException(Thread.currentThread().getName() + " was interrupted!");
            }
        };
    }

    private static void shutdownAwaitTermination(ExecutorService service) {
        try {
            service.shutdown();

            if (!service.awaitTermination(15, SECONDS)) {
                service.shutdownNow();

                if (!service.awaitTermination(15, SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}