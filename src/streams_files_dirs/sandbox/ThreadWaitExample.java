package streams_files_dirs.sandbox;

public class ThreadWaitExample {
    public static void main(String[] args) throws InterruptedException {
        String[] data = new String[1];//Let's pretend this array is some important data :D

        Thread thread = new Thread(createRunnable(data), "worker");
        thread.start();

        synchronized (data) {
            System.out.printf("Thread: %s - Starting the worker thread...%n",Thread.currentThread().getName());

            data.wait();//waits to be notified

            System.out.printf("Thread: %s - worker finished, result below.%n",Thread.currentThread().getName());
        }

        System.out.println(data[0]);//data modified
    }

    private static Runnable createRunnable(String[] out) {
        return () -> {
            simulateWork();

            synchronized (out) {
                out[0] = String.format("Thread: %s - Result: 127.0.0.1", Thread.currentThread().getName());
                out.notify();//Notifies other threads working on the data, that the resource is about to be freed
            }
        };
    }

    private static void simulateWork() {
        try {
            System.out.printf("Thread: %s - starting work...%n",Thread.currentThread().getName());
            Thread.sleep(3000);//Simulating work

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}