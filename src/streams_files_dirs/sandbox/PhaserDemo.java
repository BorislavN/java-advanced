package streams_files_dirs.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Phaser;
import java.util.concurrent.RecursiveAction;

//Phaser is a utility class for achieving thread/task synchronization
public class PhaserDemo {
    public static void main(String[] args) {
        List<String> messages = new ArrayList<>();

        ShellTask shell = new ShellTask(messages);
        shell.invoke();

        //Try to help with the tasks, until the pool is idle again
        ForkJoinTask.helpQuiesce();
    }

    public static class ShellTask extends RecursiveAction {
        private final Phaser phaser;
        private final List<String> list;

        public ShellTask(List<String> list) {
            //Because we will use this thread as the first registered, as a coordinator
            this.phaser = new Phaser(1);
            this.list = list;
        }

        @Override
        protected void compute() {
            new MessageGenerator(this.list, 10, this.phaser).fork();
            this.phaser.arriveAndAwaitAdvance();
            System.out.printf("Phase: %d, data generate.%n", this.phaser.getPhase());

            new MessageModifier(this.phaser, this.list).fork();
            this.phaser.arriveAndAwaitAdvance();
            System.out.printf("Phase: %d, data modified.%n", this.phaser.getPhase());

            new MessagePrinter(this.phaser, this.list).fork();
            this.phaser.arriveAndAwaitAdvance();
            System.out.printf("Phase: %d, data printed.%n", this.phaser.getPhase());

            //when all task are unregistered the phaser is terminated
            this.phaser.arriveAndDeregister();
        }
    }

    public static class MessageGenerator extends RecursiveAction {
        private final double count;
        private final Phaser phaser;
        private final List<String> list;

        public MessageGenerator(List<String> list, double count, Phaser phaser) {
            this.count = count;
            this.phaser = phaser;
            this.list = list;
            //register task
            this.phaser.register();
        }

        @Override
        protected void compute() {
            if (this.count > 5) {
                invokeAll(List.of(
                        new MessageGenerator(this.list, Math.floor(this.count / 2), this.phaser),
                        new MessageGenerator(this.list, Math.ceil(this.count / 2), this.phaser)
                ));
            } else {
                for (int n = 0; n < this.count; n++) {
                    this.list.add(String.format("\"%s\" - message %d", Thread.currentThread().getName(), n + 1));
                }
            }

            //unregister task
            this.phaser.arriveAndDeregister();
        }
    }

    public static class MessageModifier extends RecursiveAction {
        private final Phaser phaser;
        private final List<String> list;
        private int start;
        private int end;

        public MessageModifier(Phaser phaser, List<String> list) {
            this.phaser = phaser;
            this.list = list;
            this.phaser.register();
            this.start = -1;
            this.end = -1;
        }

        private MessageModifier(Phaser phaser, List<String> list, int start, int end) {
            this(phaser, list);
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (this.start == -1 && this.end == -1) {
                invokeAll(List.of(
                        new MessageModifier(this.phaser, this.list, 0, this.list.size() / 2),
                        new MessageModifier(this.phaser, this.list, this.list.size() / 2, this.list.size())
                ));
            } else {
                for (int index = this.start; index < this.end; index++) {
                    this.list.set(index, String.format("%s - modified by \"%s\"", this.list.get(index), Thread.currentThread().getName()));
                }
            }

            this.phaser.arriveAndDeregister();
        }
    }

    public static class MessagePrinter extends RecursiveAction {
        private final Phaser phaser;
        private final List<String> list;
        private int start;
        private int end;

        public MessagePrinter(Phaser phaser, List<String> list) {
            this.phaser = phaser;
            this.list = list;
            this.phaser.register();
            this.start = -1;
            this.end = -1;
        }

        private MessagePrinter(Phaser phaser, List<String> list, int start, int end) {
            this(phaser, list);
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (this.start == -1 && this.end == -1) {
                invokeAll(List.of(
                        new MessagePrinter(this.phaser, this.list, 0, this.list.size() / 2),
                        new MessagePrinter(this.phaser, this.list, this.list.size() / 2, this.list.size())
                ));
            } else {
                for (int index = this.start; index < this.end; index++) {
                    System.out.println(this.list.get(index));
                }
            }

            this.phaser.arriveAndDeregister();
        }
    }
}