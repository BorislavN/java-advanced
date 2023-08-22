package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class DragonArmy {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Map<String, int[]>> dragons = new LinkedHashMap<>();
        int lines = Integer.parseInt(reader.readLine());

        while (lines-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            String type = data[0];
            String name = data[1];
            int damage = setDefault(data[2], 45);
            int health = setDefault(data[3], 250);
            int armor = setDefault(data[4], 10);


            dragons.putIfAbsent(type, new TreeMap<>());
            dragons.get(type).put(name, new int[]{damage, health, armor});
        }

        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Map<String, int[]>> entry : dragons.entrySet()) {
            output.append(String.format("%s::(%.2f/%.2f/%.2f)%n"
                    , entry.getKey()
                    , getAverage(entry.getValue().values(), 0)
                    , getAverage(entry.getValue().values(), 1)
                    , getAverage(entry.getValue().values(), 2))
            );

            for (Map.Entry<String, int[]> dragon : entry.getValue().entrySet()) {
                output.append(String.format("-%s -> damage: %d, health: %d, armor: %d%n"
                        , dragon.getKey()
                        , dragon.getValue()[0]
                        , dragon.getValue()[1]
                        , dragon.getValue()[2])
                );
            }
        }

        System.out.println(output);
    }

    private static int setDefault(String input, int value) {
        return "null".equals(input) ? value : Integer.parseInt(input);
    }

    private static double getAverage(Collection<int[]> values, int index) {
        return values.stream().mapToInt(val -> val[index]).average().orElse(0);
    }
}