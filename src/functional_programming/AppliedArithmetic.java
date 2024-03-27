package functional_programming;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class AppliedArithmetic {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] numbers = Arrays.stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        StringBuilder result=new StringBuilder();
        String command;

        IntUnaryOperator add = value -> value + 1;
        IntUnaryOperator subtract = value -> value - 1;
        IntUnaryOperator multiply = value -> value * 2;

        Consumer<int[]> print= value ->{
            Arrays.stream(value).forEach(v->result.append(v).append(" "));
            result.append(System.lineSeparator());
        };

        while (!"end".equals(command = scanner.nextLine())) {
            IntUnaryOperator operation;

            switch (command) {
                case "add": operation = add;break;
                case "subtract": operation = subtract;break;
                case "multiply": operation = multiply;break;
                case "print": print.accept(numbers);continue;
                default:continue;
            }

            numbers = Arrays.stream(numbers).map(operation).toArray();
        }

        System.out.println(result.toString().trim());
    }
}