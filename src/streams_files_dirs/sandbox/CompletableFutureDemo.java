package streams_files_dirs.sandbox;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        //Each subsequent part/task is executed by the same thread
        CompletableFuture<Void> future = CompletableFuture.completedFuture("Hello World")
                .thenApply((x) -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    return x + " - EDITED";
                })
                .thenAccept((x) -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    System.out.println(x);
                });

        future.join();

        System.out.println();

        //When using the Async suffix each part/task is resubmitted tho the pool.
        //Thus, can be executed by different thread.
        CompletableFuture<Void> future2 = CompletableFuture.supplyAsync(() -> "Hello World2")
                .thenApplyAsync((x) -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    return x + " - EDITED2";
                })
                .thenAcceptAsync((x) -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    System.out.println(x);
                });

        future2.join();

        System.out.println();

        //Notice that even though we handle the exception, the future still enters the exceptionally() block
        CompletableFuture<Void> future3 = CompletableFuture.supplyAsync(() -> "Not an int :D")
                .thenApply(Integer::parseInt)
                //When is used to insert a callback function
                .whenComplete((val, ex) -> {
                    if (val == null) {
                        System.out.println("From when: Value is null!");
                    }

                    if (ex != null) {
                        System.out.println("From when: Error!");
                    }
                })
                //handle is executed every time, but can be used to handle an exception
                .handle((val, ex) -> {
                    if (ex != null) {
                        System.out.println("From handler: Error!");
                    }

                    return val + 8;
                })
                //is executed only when an exception happens
                .exceptionally((ex) -> {
                    System.out.println("From exceptionally: Error!");

                    return -1;
                })
                .thenAccept((v) -> System.out.println("From accept: " + v));

        future3.join();

        System.out.println();

        CompletableFuture<Void> future4 = CompletableFuture.supplyAsync(() -> "8")
                .thenApply(Integer::parseInt)
                .whenComplete((val, ex) -> {
                    if (val == null) {
                        System.out.println("From when: Value is null!");
                    }

                    if (ex != null) {
                        System.out.println("From when: Error!");
                    }
                })
                .handle((val, ex) -> {
                    if (ex != null) {
                        System.out.println("From handler: Error!");
                    }

                    return val + 8;
                })
                .exceptionally((ex) -> {
                    System.out.println("From exceptionally: Error!");

                    return -1;
                })
                .thenAccept((v) -> System.out.println("From accept: " + v));

        future4.join();

        System.out.println();

        //Here we skip the exceptionally() block because in the handler we wrapped the value in a new CompatibleFuture
        //And immediately joined the future to get the value
        CompletableFuture<Void> future5 = CompletableFuture.supplyAsync(() -> "Sexy boi :D")
                .thenApply(Integer::parseInt)
                .whenComplete((val, ex) -> {
                    if (val == null) {
                        System.out.println("From when: Value is null!");
                    }

                    if (ex != null) {
                        System.out.println("From when: Error!");
                    }
                })
                .handle((val, ex) -> {
                    if (ex != null) {
                        System.out.println("From handler: Error!");
                    }

                    return CompletableFuture.completedFuture(val).join();
                })
                .exceptionally((ex) -> {
                    System.out.println("From exceptionally: Error!");

                    return -1;
                })
                .thenAccept((v) -> System.out.println("From accept: " + v));

        future5.join();

        System.out.println();

        CompletableFuture<Void> future6 = CompletableFuture.supplyAsync(() -> "Sexy boi :D")
                .thenApply(Integer::parseInt)
                .whenComplete((val, ex) -> {
                    if (val == null) {
                        System.out.println("From when: Value is null!");
                    }

                    if (ex != null) {
                        System.out.println("From when: Error!");
                    }
                })
                .thenAccept((v) -> System.out.println("From accept: " + v));

        try {
            future6.join();
        } catch (Exception e) {
            System.out.println("Exception caught in " + Thread.currentThread().getName());
            System.out.println("Future6 has error: " + future6.isCompletedExceptionally());
        }

        System.out.println();

        //Notice that when exceptionally() catches the error, it is not propagated to the handle()
        //Still, handle() is executed and the value is changed
        CompletableFuture<Void> future7 = CompletableFuture.supplyAsync(() -> "Not an int :D")
                .thenApply(Integer::parseInt)
                .whenComplete((val, ex) -> {
                    if (val == null) {
                        System.out.println("From when: Value is null!");
                    }

                    if (ex != null) {
                        System.out.println("From when: Error!");
                    }
                })
                .exceptionally((ex) -> {
                    System.out.println("From exceptionally: Error!");

                    return -1;
                })
                .handle((val, ex) -> {
                    if (ex != null) {
                        System.out.println("From handler: Error!");
                    }

                    return val + 8;
                })
                .thenAccept((v) -> System.out.println("From accept: " + v));

        future7.join();

        System.out.println();

        //Compose is useful, when an api returns CompletableFuture as result
        //We can pass it on, without unwrapping
        CompletableFuture<Void> future8 = CompletableFuture.supplyAsync(() -> "1 2 3 4")
                .thenApply(v -> v.split(" "))
                //map to a matrix, the matrix is then warped in CompletableFuture
                .thenApply(v -> new String[][]{v, v})
                //flatten to array, compose returns a CompletionStage
                .thenCompose((val) -> CompletableFuture.completedStage(Stream.concat(Arrays.stream(val[0]), Arrays.stream(val[1])).toArray(String[]::new)))
                .thenAccept((v) -> System.out.println("From accept: " + Arrays.toString(v)));

        future8.join();
    }
}