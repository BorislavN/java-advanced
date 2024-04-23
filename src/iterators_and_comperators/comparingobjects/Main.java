package iterators_and_comperators.comparingobjects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Person> people = new ArrayList<>();
        String input;

        while (!"END".equals(input = reader.readLine())) {
            people.add(new Person(input.split("\\s+")));
        }

        int index = Integer.parseInt(reader.readLine());

        if (index < 0 || index >= people.size()) {
            System.out.println("No matches");

            return;
        }

        Person selected = people.get(index);

        long equal = people.stream()
                .filter(p -> p.compareTo(selected) == 0)
                .count();

        if (equal > 0) {
            System.out.printf("%d %d %d%n", equal, people.size() - equal, people.size());
        } else {
            System.out.println("No matches");
        }
    }
}
