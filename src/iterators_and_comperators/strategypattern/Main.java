package iterators_and_comperators.strategypattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        Set<Person> byName = new TreeSet<>(new NameComparator());
        Set<Person> byAge = new TreeSet<>(new AgeComparator());

        while (n-- > 0) {
            Person current = new Person(reader.readLine().split("\\s+"));

            byName.add(current);
            byAge.add(current);
        }

        byName.forEach(System.out::println);

        byAge.forEach(System.out::println);
    }
}
