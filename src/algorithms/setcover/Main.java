package algorithms.setcover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Remove package name to pass tests in judge
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] elements = reader.readLine().substring(10).split(", ");

        int[] universe = new int[elements.length];

        for (int i = 0; i < elements.length; i++) {
            universe[i] = Integer.parseInt(elements[i]);
        }

        int numberOfSets = Integer.parseInt(reader.readLine().substring(16));

        List<int[]> sets = new ArrayList<>();

        for (int i = 0; i < numberOfSets; i++) {
            String[] setElements = reader.readLine().split(", ");

            int[] set = new int[setElements.length];

            for (int j = 0; j < setElements.length; j++) {
                set[j] = Integer.parseInt(setElements[j]);
            }

            sets.add(set);
        }

        List<int[]> chosenSets = chooseSets(sets, universe);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Sets to take (%d):%n", chosenSets.size()));

        for (int[] set : chosenSets) {
            sb.append(Arrays.toString(set).replace("\\[", "{").replace("]", "}"));
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
    }

    public static List<int[]> chooseSets(List<int[]> sets, int[] universe) {
        List<Integer> elements = Arrays.stream(universe).boxed().collect(Collectors.toList());
        List<int[]> result = new ArrayList<>();

        while (!elements.isEmpty()) {
            long globalCount = 0;
            int[] chosenSet = null;

            for (int[] subset : sets) {
                long localCount = Arrays.stream(subset).filter(elements::contains).count();

                if (localCount > globalCount) {
                    globalCount = localCount;
                    chosenSet = subset;
                }
            }

            if (chosenSet != null) {
                result.add(chosenSet);

                elements.removeAll(Arrays.stream(chosenSet).boxed().collect(Collectors.toList()));
            }
        }

        return result;
    }
}
