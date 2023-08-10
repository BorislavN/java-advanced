package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class InfixToPostfix {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Deque<Character> operators = new ArrayDeque<>();
        StringBuilder output = new StringBuilder();

        for (String element : reader.readLine().split("\\s+")) {
            char character = element.charAt(0);

            //append to output
            if (Character.isLetterOrDigit(character)) {
                output.append(element).append(" ");
                continue;
            }

            //add to stack
            if ('(' == character) {
                operators.push(character);
                continue;
            }

            //append the operators between the brackets to the output and discard (
            if (')' == character) {
                while (!operators.isEmpty()) {
                    if ('(' == operators.peek()) {
                        operators.pop();

                        break;
                    }
                    output.append(operators.pop()).append(" ");
                }
                continue;
            }

            //append operators from the stack if they have equal or grater precedence
            while (!operators.isEmpty() && getPrecedence(operators.peek()) >= getPrecedence(character)) {
                output.append(operators.pop()).append(" ");
            }

            //add operator to stack
            operators.push(character);
        }

        //append the remaining operands
        while (!operators.isEmpty() && operators.peek() != '(') {
            output.append(operators.pop()).append(" ");
        }

        System.out.println(output);
    }

    public static int getPrecedence(char value) {
        if ('*' == value || '/' == value) {
            return 2;
        } else if ('+' == value || '-' == value) {
            return 1;
        } else {
            return 0;
        }
    }
}