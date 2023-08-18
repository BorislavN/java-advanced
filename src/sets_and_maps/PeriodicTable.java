package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PeriodicTable {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int lines = Integer.parseInt(reader.readLine());
        Set<String> set = new TreeSet<>();

        while (lines-- > 0) {
            set.addAll(Arrays.stream(reader.readLine().split("\\s+"))
                    .collect(Collectors.toList())
            );
        }

        System.out.println(String.join(" ", set));
    }
}