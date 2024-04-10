package defining_classes.seven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String input;

        Map<String, Person> people = new HashMap<>();

        while (!"End".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            people.putIfAbsent(data[0], new Person(data[0]));

            Person current = people.get(data[0]);

            switch (data[1]) {
                case "company":
                    double salary = Double.parseDouble(data[4]);
                    current.setCompany(new Company(data[2], data[3], salary));

                    break;

                case "car":
                    int speed = Integer.parseInt(data[3]);
                    current.setCar(new Car(data[2], speed));

                    break;

                case "pokemon":
                    current.addPokemon(new Pokemon(data[2], data[3]));

                    break;

                case "parents":
                    current.addParent(new FamilyMember(data[2], data[3]));

                    break;

                case "children":
                    current.addChild(new FamilyMember(data[2], data[3]));

                    break;
            }
        }

        System.out.println(people.get(reader.readLine()));
    }
}
