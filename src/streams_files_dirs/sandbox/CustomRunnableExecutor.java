package streams_files_dirs.sandbox;

//Here i will try to implement something resembling Future
//I wrapped up the Runnable run() method
//And now I'm able to catch the exceptions thrown in different threads
public class CustomRunnableExecutor {
    public static void main(String[] args) {
        Task task1 = new Task(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(Thread.currentThread().getName() + " WORK DONE!");
        });

        Task task2 = new Task(() -> {
            try {
                Thread.sleep(2000);
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
    private final Runnable runnable;
    private final ExceptionHandler handler;

    Task(Runnable runnable) {
        this.lock = new Object();
        this.runnable = runnable;
        this.handler = new ExceptionHandler(this.lock);
    }

    //Blocks until the result is ready, just like Future
    public void get() throws InterruptedException, IllegalStateException {
        synchronized (this.lock) {
            Thread worker = new Thread(new ExtendedRunnable(this.runnable, this.lock));
            worker.setUncaughtExceptionHandler(this.handler);
            worker.start();

            //Even if the thread wakes up spontaneously
            //We wait for the thread to exit
            //So we don't miss an error, if thrown
            this.lock.wait();

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
        synchronized (this.lock) {
            this.runnable.run();

            //we skip the notify call in case of error
            //we call it in the error handler
            this.lock.notifyAll();
        }
    }
}

class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Object lock;
    private Throwable exception;

    public ExceptionHandler(Object lock) {
        this.lock = lock;
        this.exception = null;
    }

    //we set the error
    //and call notify, because we skipped it the run()
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        synchronized (this.lock) {
            this.exception = e;
            this.lock.notifyAll();
            t.interrupt();//the thread should be closing, but we interrupt it just in case
        }
    }

    public Throwable getException() {
        synchronized (this.lock) {
            return this.exception;
        }
    }
}
