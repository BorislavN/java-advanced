package streams_files_dirs.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

//Depending on how threads starts up, the output will be different
//Run a few times to see the differences
public class StampedLockDemo {
    public static void main(String[] args) {
        StampedArray array = new StampedArray();
        array.add("Line 1");
        array.add("Line 2");
        array.add("Line 3");

        Thread thread1 = newThread(() -> {
            System.out.println(array);
            for (int i = 0; i < 10; i++) {
                System.out.println(array.get(2));
            }
        }, "Thread-1");

        Thread thread2 = newThread(() -> {
            array.add("Line 4");

            for (int i = 0; i < 50; i++) {
                array.add("Line 5");
                array.remove(0);
            }
        }, "Thread-2");

        Thread thread3 = newThread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(array.get(0));
            }

            System.out.println(array);

        }, "Thread-3");

        //The execution order is not guaranteed to be the same as the start order
        thread1.start();
        thread2.start();
        thread3.start();
    }

    private static Thread newThread(Runnable runnable, String name) {
        return new Thread(runnable, name);
    }

    //Synchronized collection with StampedLock
    private static class StampedArray {
        private final StampedLock lock;
        private final List<String> array;

        public StampedArray() {
            this.array = new ArrayList<>();
            this.lock = new StampedLock();
        }

        //acquires a write-lock and adds the element
        public void add(String element) {
            long stamp = this.lock.writeLock();

            try {
                this.array.add(element);
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }

        //Tries optimistic read, if the collection is being modified - returns a string to inform us
        public String get(int index) {
            long stamp = this.lock.tryOptimisticRead();

            try {
                if (stamp == 0 || !this.lock.validate(stamp)) {
                    return "ARRAY IS BEING MODIFIED";
                }

                return this.array.get(index);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        //acquires a write-lock and removes the element
        public String remove(int index) {
            long stamp = this.lock.writeLock();

            try {
                return this.array.remove(index);
            } catch (IndexOutOfBoundsException e) {
                return null;
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }

        //acquires a read-lock and returns the string
        @Override
        public String toString() {
            long stamp = this.lock.readLock();

            try {
                return this.array.toString();
            } finally {
                this.lock.unlock(stamp);
            }
        }
    }
}