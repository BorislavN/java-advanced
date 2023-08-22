package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

//Having a dedicated map for totalPopulation speeds up the algorithm slightly
//by circumventing the .map and .reduce functions
public class PopulationCounterV2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Map<String, Integer>> countries = new LinkedHashMap<>();
        Map<String, BigInteger> totalPopulation = new LinkedHashMap<>();
        String input;

        while (!"report".equals(input = reader.readLine())) {
            String[] data = input.split("\\|");

            String city = data[0];
            String country = data[1];
            int population = Integer.parseInt(data[2]);

            countries.putIfAbsent(country, new LinkedHashMap<>());
            countries.get(country).putIfAbsent(city, population);

            totalPopulation.putIfAbsent(country, BigInteger.ZERO);
            totalPopulation.put(country, totalPopulation.get(country).add(BigInteger.valueOf(population)));
        }

        StringBuilder output = new StringBuilder();

        countries.entrySet().stream()
                .sorted((f, s) -> totalPopulation.get(s.getKey()).compareTo(totalPopulation.get(f.getKey())))
                .forEach(entry -> {
                    output.append(String.format("%s (total population: %d)%n"
                            , entry.getKey()
                            , totalPopulation.get(entry.getKey()))
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
}