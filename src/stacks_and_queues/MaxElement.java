package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

public class MaxElement {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArrayDeque<Integer> stack = new ArrayDeque<>();
        int lines = Integer.parseInt(reader.readLine());

        while (lines-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            switch (data[0]) {
                case "1":
                    stack.push(Integer.parseInt(data[1]));
                    break;
                case "2":
                    stack.pop();
                    break;
                case "3":
                    System.out.println(stack.stream().max(Integer::compareTo).orElse(0));
                    break;
            }
        }
    }
}