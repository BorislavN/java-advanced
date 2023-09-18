package streams_files_dirs.sandbox;

import java.util.Arrays;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

//An extension of ForkJoinTask, better suited for handling stalling/blocking tasks
//It has an onCompletion and onExceptionalCompletion callbacks, useful for performing an action after task/subtask completion
public class CountedCompleterDemo {
    public static void main(String[] args) {
        String[] messages = {"message1", "message2", "message3", "message4", "message5", "message6", "message7", "message8", "message9", "message10",};

        MessagePrinter printer = new MessagePrinter(null, messages, 0, messages.length);
        MessageSearcher searcher = new MessageSearcher(messages, "message7");
        MessageSearcher searcher2 = new MessageSearcher(messages, "message69");

        printer.invoke();
        String result = searcher.invoke();
        String result2 = searcher2.invoke();

        //The current thread tries to help with the work, till the pool is idle again
        ForkJoinTask.helpQuiesce();

        System.out.println(Arrays.toString(messages));
        System.out.println();
        System.out.println("Searching for message7, found - " + result);
        System.out.println();
        System.out.println("Searching for message69, found - " + result2);
    }

    private static class MessagePrinter extends CountedCompleter<Void> {
        private final String[] array;
        private final int start;
        private volatile int end;
        private volatile String current;

        public MessagePrinter(CountedCompleter<?> completer, String[] array, int start, int end) {
            super(completer);
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public void compute() {
            //Creating subtasks and incrementing pending count
            while ((this.end - this.start) >= 2) {
                int middle = (this.start + this.end) / 2;

                MessagePrinter completer = new MessagePrinter(this, this.array, middle, this.end);
                this.addToPendingCount(1);
                completer.fork();

                this.end = middle;
            }

            //Performing the "business" logic and decrementing the count
            if (this.end > this.start) {
                this.array[this.start] = this.array[this.start].toUpperCase();
                this.current = this.array[this.start];

                this.tryComplete();
            }
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            //Synchronize so the output is in correct format
            //Without sync the lines can be mixed around
            synchronized (System.out) {
                CountedCompleter<?> parent = this.getCompleter();

                System.out.println("Thread: " + Thread.currentThread().getName());
                System.out.println("Current element: " + this.current);
                System.out.println("Parent completer: " + (parent != null ? parent.toString().substring(parent.toString().lastIndexOf('@')) : null));
                System.out.println();
            }
        }
    }

    private static class MessageSearcher extends CountedCompleter<String> {
        private final String[] array;
        private final AtomicReference<String> result;
        private String value;
        private final int start;
        private final int end;

        public MessageSearcher(String[] array, String value) {
            super(null);
            this.array = array;
            this.result = new AtomicReference<>();
            this.setValue(value);
            this.start = 0;
            this.end = this.array.length;
        }

        private MessageSearcher(CountedCompleter<?> completer, String[] array, AtomicReference<String> result, String value, int start, int end) {
            super(completer);
            this.array = array;
            this.result = result;
            this.setValue(value);
            this.start = start;
            this.end = end;
        }

        private void setValue(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Search value can not be null!");
            }

            this.value = value;
        }

        @Override
        public void compute() {
            //copy the variables locally, could have used volatile
            int localStart = this.start;
            int localEnd = this.end;

            //create subtasks
            while (!canNotBeSplitFurther(localStart, localEnd)) {
                int middle = (localStart + localEnd) / 2;

                MessageSearcher subtask = new MessageSearcher(this, this.array, this.result, this.value, middle, localEnd);
                this.addToPendingCount(1);
                subtask.fork();

                localEnd = middle;
            }

            //check if the current element matches the value, if none was found
            if (this.getRawResult() == null) {
                String currentElement = this.array[localStart];

                if (this.matches(currentElement) && this.result.compareAndSet(null, currentElement)) {
                    //complete the root task
                    this.quietlyCompleteRoot();
                }
            }

            //complete normally
            this.propagateCompletion();
        }

        private boolean canNotBeSplitFurther(int start, int end) {
            return Math.abs(start - end) == 1;
        }

        @Override
        public String getRawResult() {
            return this.result.get();
        }

        //Check for equality case-insensitive
        private boolean matches(String current) {
            return this.value.equalsIgnoreCase(current);
        }
    }
}