package generics.eleven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] one = readData(reader);
        Threeuple<String, String, String> first = new Threeuple<>(String.format("%s %s", one[0], one[1]), one[2], one[3]);

        String[] two = readData(reader);
        boolean drunk = "drunk".equals(two[2]);

        Threeuple<String, Integer, Boolean> second = new Threeuple<>(two[0], Integer.parseInt(two[1]), drunk);

        String[] three = readData(reader);
        Threeuple<String, Double, String> third = new Threeuple<>(three[0], Double.parseDouble(three[1]), three[2]);

        System.out.println(first);
        System.out.println(second);
        System.out.println(third);
    }

    private static String[] readData(BufferedReader reader) throws IOException {
        return reader.readLine().split("\\s+");
    }
}
