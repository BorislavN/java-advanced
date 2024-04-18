package generics.ten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] one = readData(reader);
        Tuple<String, String> first = new Tuple<>(String.format("%s %s", one[0], one[1]), one[2]);

        String[] two = readData(reader);
        Tuple<String, Integer> second = new Tuple<>(two[0], Integer.parseInt(two[1]));

        String[] three = readData(reader);
        Tuple<Integer, Double> third = new Tuple<>(Integer.parseInt(three[0]), Double.parseDouble(three[1]));

        System.out.println(first);
        System.out.println(second);
        System.out.println(third);
    }

    private static String[] readData(BufferedReader reader) throws IOException {
        return reader.readLine().split("\\s+");
    }
}