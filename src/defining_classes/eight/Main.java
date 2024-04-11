package defining_classes.eight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String personToken = reader.readLine();
        Person person = null;

        List<String> relations = new ArrayList<>();
        List<Person> relatives = new ArrayList<>();

        String input;

        while (!"End".equals(input = reader.readLine())) {
            if (!input.contains("-")) {
                String[] data = input.split("\\s+");

                Person current = new Person(data[0], data[1], data[2]);

                if (current.toString().contains(personToken)) {
                    person = current;
                }

                relatives.add(current);

                continue;
            }

            relations.add(input);
        }

        if (person == null) {
            return;
        }

        for (String relation : relations) {
            String[] data = relation.split("\\s+-\\s+");

            BiPredicate<Person, String> filter = (p, token) -> p.toString().contains(token);

            Person parent = relatives.stream()
                    .filter(r -> filter.test(r, data[0]))
                    .findAny()
                    .orElse(null);

            Person child = relatives.stream()
                    .filter(r -> filter.test(r, data[1]))
                    .findAny()
                    .orElse(null);

            if (parent != null && child != null) {
                parent.addChild(child);
                child.addParent(parent);
            }
        }

        System.out.println(Person.getTree(person));
    }
}