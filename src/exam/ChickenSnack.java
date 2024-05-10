package exam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ChickenSnack {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//        //While we are adding elements using the correct methods for stack and queue, there is no problem
//        //A problem will arise when using Collectors.toCollection(ArrayDeque::new) -> the elements are added as in a queue
//        //And when we call the stack methods on the deque they will no longer behave how we expect
//        //I got stuck on this easy problem, because of that...
//        ArrayDeque<String> stack = new ArrayDeque<>();
//        ArrayDeque<String> queue = new ArrayDeque<>();
//
//        for (String element : reader.readLine().split("\\s+")) {
//            stack.push(element);
//            queue.offer(element);
//        }
//
//        //No problem
//        while (!stack.isEmpty() && !queue.isEmpty()) {
//            System.out.printf("stack(%s) <=> queue(%s)%n", stack.pop(), queue.poll());
//        }

        ArrayDeque<Integer> moneyStack = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayDeque::new));

        ArrayDeque<Integer> priceQueue = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayDeque::new));

        int foodsEaten = 0;

        while ((!moneyStack.isEmpty()) && (!priceQueue.isEmpty())) {
            int currentMoney = moneyStack.removeLast();
            int currentPrice = priceQueue.removeFirst();

            int change = currentMoney - currentPrice;

            if (change >= 0) {
                foodsEaten++;
            }

            if (change > 0) {
                if (!moneyStack.isEmpty()) {
                    //If we don't use addLast, but instead push, the code no longer passes the judge tests
                    moneyStack.addLast(moneyStack.removeLast() + change);
                }
            }
        }

        if (foodsEaten >= 4) {
            System.out.printf("Gluttony of the day! Henry ate %d foods.", foodsEaten);
        }

        if (foodsEaten >= 1 && foodsEaten < 4) {
            System.out.printf("Henry ate: %d %s.%n", foodsEaten, foodsEaten == 1 ? "food" : "foods");
        }

        if (foodsEaten == 0) {
            System.out.println("Henry remained hungry. He will try next weekend again.");
        }
    }
}