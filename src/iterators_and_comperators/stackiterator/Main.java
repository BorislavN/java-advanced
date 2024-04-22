package iterators_and_comperators.stackiterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Stack<Integer> stack = new Stack<>();

        String input;

        while (!"END".equals(input = reader.readLine())) {
            if (input.startsWith("Push")) {
                Arrays.stream(input.split("[\\s,]+"))
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .forEach(stack::push);
            }

            if ("Pop".equals(input)) {
                Integer value = stack.pop();

                if (value == null) {
                    System.out.println("No elements");
                }
            }
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            stack.forEach(e -> output.append(e).append(System.lineSeparator()));
        }

        System.out.println(output.toString().trim());
    }
}