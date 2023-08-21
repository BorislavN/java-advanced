package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class HandsOfCards {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Set<String>> map = new LinkedHashMap<>();

        String input;

        while (!"JOKER".equals(input = reader.readLine())) {
            String[] data = input.split(":\\s+");

            map.putIfAbsent(data[0], new HashSet<>());

            map.get(data[0]).addAll(Arrays.stream(data[1].split(",\\s+"))
                    .collect(Collectors.toList())
            );
        }

        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            output.append(String.format("%s: %d%n"
                    , entry.getKey()
                    , entry.getValue().stream().mapToInt(HandsOfCards::getValue).sum())
            );
        }

        System.out.println(output);
    }

    private static int getValue(String card) {
        String types = "_CDHS";
        String powers = "_JQKA";

        String power = card.substring(0, card.length() - 1);
        int type = types.indexOf(card.charAt(card.length() - 1));

        int powerValue = 10 + powers.indexOf(power);

        if (powerValue < 11) {
            powerValue = Integer.parseInt(power);
        }

        return powerValue * type;
    }
}