package streams_files_dirs.sandbox;

//Demo to show that exceptions thrown from threads, can't be caught in the main thread
//But we can have centralised ExceptionHandler processing the exceptions from the threads
public class ThreadExceptionDemo {
    public static void main(String[] args) {
        Handler handler = new Handler();
        Thread.setDefaultUncaughtExceptionHandler(handler);

        Thread thread1 = createThread("One", 2000);
        Thread thread2 = createThread("Two", 1000);

        try {
            thread1.start();
            thread2.start();
        } catch (IllegalStateException e) {
            System.out.println("Exception caught!");
        }
    }

    public static Thread createThread(String name, int timeout) {
        return new Thread(() -> {
            try {
                Thread.sleep(timeout);
                throw new RuntimeException(Thread.currentThread().getName() + " Oops...");
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted");
            }
        }, name);
    }

    //We can define an exception handler this way
    private static class Handler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Exception handler - exception detected: " + e.getMessage());
            System.out.println(t.getName());
            System.out.println(t.isAlive());
            throw new IllegalStateException(e);
        }
    }
}
