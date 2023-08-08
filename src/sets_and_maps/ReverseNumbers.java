package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;

public class ReverseNumbers {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArrayDeque<String> deque = new ArrayDeque<>();
        Arrays.stream(reader.readLine().split("\\s+")).forEach(deque::push);

        deque.iterator().forEachRemaining(e -> System.out.print(e + " "));
    }
}