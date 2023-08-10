package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Fibonacci {
    private static long[] memory;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int number = Integer.parseInt(reader.readLine());
        memory = new long[number + 2];

        System.out.println(getFibonacci(number + 1));
    }

    private static long getFibonacci(int number) {
        if (number == 0) {
            return 0;
        }

        if (number <= 2) {
            return 1;
        }

        if (memory[number] == 0) {
            memory[number] = getFibonacci(number - 1) + getFibonacci(number - 2);
        }

        return memory[number];
    }
}