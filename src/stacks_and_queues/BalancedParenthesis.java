package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

public class BalancedParenthesis {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean isBalanced = true;
        String[] data = reader.readLine().split("");

        ArrayDeque<String> opening = new ArrayDeque<>();

        if (data.length % 2 != 0) {
            isBalanced = false;
        }

        if (isBalanced) {
            for (String element : data) {
                if ("(".equals(element) || "{".equals(element) || "[".equals(element)) {
                    opening.push(element);
                }

                if (")".equals(element) || "}".equals(element) || "]".equals(element)) {
                    isBalanced = checkForEquality(opening.pop() + element);

                    if (!isBalanced) {
                        break;
                    }
                }
            }
        }

        System.out.println(isBalanced && opening.isEmpty() ? "YES" : "NO");
    }

    private static boolean checkForEquality(String value) {
        return ("()".equals(value)) || ("{}".equals(value)) || ("[]".equals(value));
    }
}