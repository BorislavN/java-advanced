package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class FixEmails {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, String> map = new LinkedHashMap<>();
        String name;

        String[] domains = {"us", "uk", "com"};

        while (!"stop".equals(name = reader.readLine())) {
            String email = reader.readLine();

            if (Arrays.stream(domains).noneMatch(email::endsWith)) {
                map.put(name, email);
            }
        }

        StringBuilder output = new StringBuilder();

        map.forEach((k, v) -> output.append(String.format("%s -> %s%n", k, v)));

        System.out.println(output);
    }
}