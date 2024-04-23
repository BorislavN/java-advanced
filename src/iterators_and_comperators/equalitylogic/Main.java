package iterators_and_comperators.equalitylogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        Set<Person>treeSet=new TreeSet<>();
        Set<Person>hashSet=new HashSet<>();

        while (n-- > 0) {
            String[] data = reader.readLine().split("\\s+");
            Person person = new Person(data);

            treeSet.add(person);
            hashSet.add(person);
        }

        System.out.println(treeSet.size());
        System.out.println(hashSet.size());
    }
}
