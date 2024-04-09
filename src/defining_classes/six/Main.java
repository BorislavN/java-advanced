package defining_classes.six;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String input;
        Map<String, Trainer> trainers = new LinkedHashMap<>();

        while (!"Tournament".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            trainers.putIfAbsent(data[0], new Trainer(data[0]));

            int health = Integer.parseInt(data[3]);

            trainers.get(data[0]).catchPokemon(new Pokemon(data[1], data[2], health));
        }

        while (!"End".equals(input = reader.readLine())) {
            for (Trainer trainer : trainers.values()) {
                trainer.fight(input);
            }
        }

        trainers.values().stream()
                .sorted(Comparator.comparing(Trainer::getBadges).reversed())
                .forEach(System.out::println);
    }
}
