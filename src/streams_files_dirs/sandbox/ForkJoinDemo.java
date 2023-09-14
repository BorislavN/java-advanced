package streams_files_dirs.sandbox;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

//Task can be run standalone(it will use the default pool)
//or with a specific pool

//To arrange the execution of a task we call fork, submit or execute
//To get the result of a task, we can use join, get, invoke(submits the task for execution and waits for the task to finish in one method call)
public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        List<String> messages = List.of("Encode this :D", "Epic message baby", "Tiger lover", "Panther tanker", "League of legends", "IDK anymore", "hahahahhah ha");

        //Task invoked outside a pool, it uses the common pool by default
        VoidTask task = new VoidTask(messages, 3);
        task.invoke();

        System.out.println();

        //The same task invoked form the pool, with a different key
        VoidTask task1 = new VoidTask(messages, 50);
        pool.invoke(task1);

        System.out.println();

        Instant start = Instant.now();

        ValueTask task2 = new ValueTask(new MOBAData(
                new int[]{5000, -2000, 3000, 500, 2905},
                new int[]{40, 25, 36, 55, 15},
                new int[]{3, 5, 2, 1, 6},
                new int[]{15, 10, 8, 12, 9}
        ));

        for (CalculationResult calculationResult : pool.invoke(task2)) {
            System.out.println(calculationResult);
        }

        Instant end = Instant.now();

        System.out.println("Time in ms: " + Duration.between(start, end).toMillis());
    }

    private static class VoidTask extends RecursiveAction {
        private final List<String> messages;
        private final int key;
        private final int threshold;

        public VoidTask(List<String> text, int key) {
            this.messages = text;
            this.key = key;
            this.threshold = 5;
        }

        @Override
        protected void compute() {
            if (this.messages.size() > this.threshold) {
                invokeAll(this.divideTask());
            } else {
                this.process(this.messages);
            }
        }

        private List<VoidTask> divideTask() {
            List<VoidTask> temp = new ArrayList<>();

            for (int index = 0; index < this.messages.size(); index += this.messages.size() / 4) {
                temp.add(new VoidTask(this.messages.subList(index, Math.min(index + this.messages.size() / 4, this.messages.size())), this.key));
            }

            return temp;
        }

        private void process(List<String> sublist) {
            for (String msg : sublist) {
                System.out.printf("%s - encrypted \"%s\" to \"%s\"%n"
                        , Thread.currentThread().getName()
                        , msg
                        , msg.chars().mapToObj(n -> Character.toString(this.shiftNumber(n))).collect(Collectors.joining()));
            }
        }

        private int shiftNumber(int number) {
            int temp = (number + this.key) % 126;

            return temp < 32 ? 32 + temp : temp;
        }
    }

    private static class ValueTask extends RecursiveTask<List<CalculationResult>> {
        private final MOBAData data;
        private final String name;
        private final int[] score;

        private ValueTask(MOBAData data) {
            this.data = data;
            this.name = null;
            this.score = new int[0];
        }

        public ValueTask(String name, int[] score) {
            this.data = null;
            this.name = name;
            this.score = score;
        }

        @Override
        protected List<CalculationResult> compute() {
            if (this.name == null) {
                return this.divideTask()
                        .parallelStream()
                        .map(ForkJoinTask::invoke)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            return this.processData();
        }

        private List<ValueTask> divideTask() {
            if (this.data == null) {
                return new ArrayList<>();
            }

            return List.of(new ValueTask("Average Epic monsters", this.data.epicMonsters())
                    , new ValueTask("Average gold difference", this.data.goldDifference())
                    , new ValueTask("Average Match time", this.data.matchesTime())
                    , new ValueTask("Average structures destroyed", this.data.structuresDestroyed()));
        }

        private List<CalculationResult> processData() {
            //Simulating delay
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return List.of(new CalculationResult(Thread.currentThread().getName(), this.name, Arrays.stream(this.score).average().orElse(0)));
        }
    }

    private record CalculationResult(String threadName, String name, double average) {
        @Override
        public String toString() {
            return String.format("%s: %s - %.2f", this.threadName, this.name, this.average);
        }
    }

    private record MOBAData(int[] goldDifference, int[] matchesTime, int[] epicMonsters, int[] structuresDestroyed) {
    }
}