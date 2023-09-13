package streams_files_dirs.sandbox;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();


        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " work done!");
        });


        //TODO: This will be a demo for fork/join tasks
        //Currently still reading about the API

        //Task can be run standalone(it will use the default pool)
        //or with a specific pool

        //To arrange the execution of a task we call fork, submit or execute
        //To get the result of a task, we can use join, get, invoke(submits the task for execution and waits for the task to finish in one method call)
    }
}