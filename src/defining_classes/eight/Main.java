package defining_classes.eight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String personToken = reader.readLine();

        List<String> relations = new ArrayList<>();
        Map<String, Person> relativesByName = new HashMap<>();
        Map<String, Person> relativesByBirthday = new HashMap<>();

        String input;

        while (!"End".equals(input = reader.readLine())) {
            if (!input.contains("-")) {
                String[] data = input.split("\\s+");

                Person current = new Person(data[0], data[1], data[2]);

                relativesByName.put(current.getNames(), current);
                relativesByBirthday.put(data[2], current);

                continue;
            }

            relations.add(input);
        }

        Function<String, Person> find = (token) -> {
            Person current = relativesByName.get(token);

            if (current == null) {
                return relativesByBirthday.get(token);
            }

            return current;
        };

        for (String relation : relations) {
            String[] data = relation.split("\\s+-\\s+");

            Person parent = find.apply(data[0]);
            Person child = find.apply(data[1]);

            if (parent != null && child != null) {
                parent.addChild(child);
                child.addParent(parent);
            }
        }

        System.out.println(Person.getTree(find.apply(personToken)));
    }
}