package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CountSymbols {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<Character, Integer> map = reader.readLine().chars()
                .mapToObj(e -> Character.toString(e).charAt(0))
                .collect(Collectors.toMap((k) -> k, (v) -> 1, (o, n) -> o + 1, TreeMap::new));

        StringBuilder output = new StringBuilder();

        map.keySet().forEach(k -> output.append(String.format("%c: %d time/s%n", k, map.get(k))));

        System.out.println(output);
    }
}