package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class PopulationCounter {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Map<String, Integer>> countries = new LinkedHashMap<>();
        String input;

        while (!"report".equals(input = reader.readLine())) {
            String[] data = input.split("\\|");

            String city = data[0];
            String country = data[1];
            int population = Integer.parseInt(data[2]);

            countries.putIfAbsent(country, new LinkedHashMap<>());
            countries.get(country).putIfAbsent(city, population);
        }

        StringBuilder output = new StringBuilder();

        countries.entrySet().stream()
                .sorted((f, s) -> getTotalPopulation(s.getValue()).compareTo(getTotalPopulation(f.getValue())))
                .forEach(entry -> {
                    output.append(String.format("%s (total population: %d)%n"
                            , entry.getKey()
                            , getTotalPopulation(entry.getValue()))
                    );

                    entry.getValue().entrySet().stream()
                            .sorted((f, s) -> Integer.compare(s.getValue(), f.getValue()))
                            .forEach(element -> {
                                output.append(String.format("=>%s: %d%n"
                                        , element.getKey()
                                        , element.getValue()));
                            });
                });

        System.out.println(output);
    }

    private static BigInteger getTotalPopulation(Map<String, Integer> country) {
        return country.values().stream()
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}