package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MatrixRotation {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int degrees = Integer.parseInt(reader.readLine().replaceAll("\\D", ""));
        int maxLength = 0;

        String input;
        List<String> list = new ArrayList<>();

        while (!"END".equals(input = reader.readLine())) {
            list.add(input);

            if (input.length() > maxLength) {
                maxLength = input.length();
            }
        }

        //TODO
    }
}