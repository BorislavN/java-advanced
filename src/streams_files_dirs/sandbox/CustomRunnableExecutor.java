package streams_files_dirs.sandbox;

//Here I will try to implement something resembling Future
//Currently the code successfully catches  the exceptions thrown in different threads
public class CustomRunnableExecutor {
    public static void main(String[] args) {
        Task task1 = new Task(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(Thread.currentThread().getName() + " WORK DONE!");
        });

        Task task2 = new Task(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            throw new RuntimeException(Thread.currentThread().getName() + " ERROR!!!");
        });

        try {
            task1.get();
            task2.get();
        } catch (InterruptedException | IllegalStateException e) {
            System.err.println("Exception caught - " + e.getMessage());
        }
    }
}

class Task {
    private final Object lock;
    private final ExceptionHandler handler;
    private final Thread worker;

    Task(Runnable runnable) {
        this.lock = new Object();
        this.handler = new ExceptionHandler(this.lock);
        this.worker = new Thread(new ExtendedRunnable(runnable, this.lock));
        worker.setUncaughtExceptionHandler(this.handler);

        this.start();
    }

    //starts the thread when the class is initialized
    private void start() {
        worker.start();
    }

    //Blocks until the result is ready, just like Future
    //Task will start executing by default
    //But if we don't call get() - we wont propagate the exceptions to the main thread
    public void get() throws InterruptedException, IllegalStateException {
        synchronized (this.lock) {
            //Wait for the thread to stop
            while (worker.isAlive()) {
                this.lock.wait(500);
            }

            if (this.handler.getException() != null) {
                System.out.println(worker.getName() + " is alive: " + worker.isAlive());
                throw new IllegalStateException(this.handler.getException());
            }
        }
    }
}

record ExtendedRunnable(Runnable runnable, Object lock) implements Runnable {
    @Override
    public void run() {
        this.runnable.run();
        Thread.currentThread().interrupt();
    }
}

class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Object lock;
    private Throwable exception;

    public ExceptionHandler(Object lock) {
        this.lock = lock;
        this.exception = null;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.exception = e;
        t.interrupt();//the thread should be closing, but we interrupt it just in case
    }

    public Throwable getException() {
        synchronized (this.lock) {
            return this.exception;
        }
    }
}
