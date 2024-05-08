package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Factorial {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        System.out.println(calculateFactorial(n));
    }

    private static long calculateFactorial(int number) {
        if (number <= 1) {
            return 1;
        }

        return number * calculateFactorial(--number);
    }
}
