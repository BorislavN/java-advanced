package sets_and_maps;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TreeSetPlayground {
    public static void main(String[] args) throws IOException {
        Set<Person> treeSet = new TreeSet<>();
        treeSet.add(new Person(2, "Ivan"));
        treeSet.add(new Person(3, "Pesho"));
        treeSet.add(new Person(1, "Gosho"));
        treeSet.add(new Person(2, "Stamat"));//duplicate id
        treeSet.add(new Person(4, "Ivan"));//duplicate name

        System.out.println("Using the Comparable interface:");
        treeSet.forEach(System.out::println);

        System.out.println();
        System.out.println("Using Comparator to sort:");

        treeSet.stream()
                .sorted(new ByNamePersonComparator())
                .forEach(System.out::println);

        System.out.println();
        System.out.println("Original set:");

        treeSet.forEach(System.out::println);

        //the same elements but with the new comparator
        treeSet = new TreeSet<>(new ByNamePersonComparator());
        treeSet.add(new Person(2, "Ivan"));
        treeSet.add(new Person(3, "Pesho"));
        treeSet.add(new Person(1, "Gosho"));
        treeSet.add(new Person(2, "Stamat"));//duplicate id
        treeSet.add(new Person(4, "Ivan"));//duplicate name

        System.out.println();
        System.out.println("Initialized with the Comparator:");

        treeSet.forEach(System.out::println);

        //The Comparator besides being used to sort the set
        //is used to compare the elements and remove the duplicates
        //this is way in the first case we have unique ids
        //and in the second unique names
    }

    private record Person(int id, String name) implements Comparable<Person> {
        @Override
        public int compareTo(Person other) {
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return String.format("Id: %d Name: %s", this.id, this.name);
        }
    }

    private static class ByNamePersonComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            return o1.name().compareTo(o2.name());
        }
    }
}