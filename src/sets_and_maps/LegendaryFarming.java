package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class LegendaryFarming {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, String> legendaryItems = Map.of(
                "shards", "Shadowmourne",
                "fragments", "Valanyr",
                "motes", "Dragonwrath"
        );

        Map<String, Integer> loot = new TreeMap<>();

        loot.put("shards", 0);
        loot.put("fragments", 0);
        loot.put("motes", 0);

        boolean craftedLegendary = false;

        StringBuilder output = new StringBuilder();

        while (!craftedLegendary) {
            String[] data = reader.readLine().split("\\s+");

            for (int index = 0; index < data.length; index += 2) {
                int amount = Integer.parseInt(data[index]);
                String item = (data[index + 1]).toLowerCase();

                loot.putIfAbsent(item, 0);
                loot.put(item, loot.get(item) + amount);

                if ("shards".equals(item) || "fragments".equals(item) || "motes".equals(item)) {
                    if (loot.get(item) >= 250) {
                        output.append(String.format("%s obtained!%n", legendaryItems.get(item)));
                        craftedLegendary = true;

                        loot.put(item, loot.get(item) - 250);

                        break;
                    }
                }
            }
        }

        loot.entrySet().stream()
                .sorted((e1, e2) -> {
                    int oneFlag = legendaryItems.containsKey(e1.getKey()) ? 1 : 0;
                    int twoFlag = legendaryItems.containsKey(e2.getKey()) ? 1 : 0;

                    int value = twoFlag - oneFlag;

                    if (value == 0 && oneFlag == 1 && twoFlag == 1) {
                        value = Integer.compare(e2.getValue(), e1.getValue());
                    }

                    return value;
                })
                .forEach(entry -> output.append(String.format("%s: %d%n"
                        , entry.getKey()
                        , entry.getValue()))
                );

        System.out.println(output);
    }
}