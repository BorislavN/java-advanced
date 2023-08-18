package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Phonebook {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, String> phonebook = new HashMap<>();
        String input;

        while (!"search".equals(input = reader.readLine())) {
            String[] data = input.split("-");

            phonebook.put(data[0], data[1]);
        }

        StringBuilder output = new StringBuilder();

        while (!"stop".equals(input = reader.readLine())) {
            if (phonebook.containsKey(input)) {
                output.append(String.format("%s -> %s%n", input, phonebook.get(input)));

                continue;
            }

            output.append(String.format("Contact %s does not exist.%n", input));
        }

        System.out.println(output);
    }
}