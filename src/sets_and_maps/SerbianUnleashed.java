package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerbianUnleashed {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Map<String, Integer>> venues = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("([\\w ]+) @([\\w ]+) (\\d+) (\\d+)");
        String input;

        while (!"End".equals(input = reader.readLine())) {
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                String singer = matcher.group(1);
                String venue = matcher.group(2);
                int price = Integer.parseInt(matcher.group(3));
                int tickets = Integer.parseInt(matcher.group(4));

                venues.putIfAbsent(venue, new LinkedHashMap<>());
                venues.get(venue).putIfAbsent(singer, 0);
                venues.get(venue).merge(singer, price * tickets, Integer::sum);
            }
        }

        StringBuilder output = new StringBuilder();

        for (String key : venues.keySet()) {
            output.append(key).append(System.lineSeparator());

            venues.get(key).entrySet().stream()
                    .sorted((s1, s2) -> Integer.compare(s2.getValue(), s1.getValue()))
                    .forEach(entry -> output.append(String.format("#  %s -> %d%n"
                            , entry.getKey()
                            , entry.getValue())));
        }

        System.out.println(output);
    }

    private static int indexOfVenue(String[] data) {
        for (int index = 0; index < data.length; index++) {
            String element = data[index];
            if (element.charAt(0) == '@') {
                return index;
            }
        }

        return -1;
    }
}