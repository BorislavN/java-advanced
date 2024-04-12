package defining_classes.nine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Cat> cats = new HashMap<>();

        String input;

        while (!"End".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            Cat cat;

            switch (data[0]) {
                case "Siamese":
                    cat = new Siamese(data[1], Double.parseDouble(data[2]));
                    break;
                case "Cymric":
                    cat = new Cymric(data[1], Double.parseDouble(data[2]));
                    break;
                default:
                    cat = new StreetExtraordinaire(data[1], Double.parseDouble(data[2]));
                    break;
            }

            cats.putIfAbsent(cat.getName(), cat);
        }

        System.out.println(cats.get(reader.readLine()));
    }
}
