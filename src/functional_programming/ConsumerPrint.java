package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Consumer;

public class ConsumerPrint {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Consumer<String> printer = (String value) -> {
            Arrays.stream(value.split("\\s+")).forEach(System.out::println);
        };

        printer.accept(reader.readLine());
    }
}