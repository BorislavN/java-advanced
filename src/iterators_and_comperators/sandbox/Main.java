package iterators_and_comperators.sandbox;

import java.util.Arrays;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();

        //One
        printTestName("Addition");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add(0, "0.5");
        list.add(2, "1.5");
        list.add(4, "2.5");
        list.add(6, "3.5");
        System.out.println(list);
        printSeparator();

        //Two
        printTestName("Get");
        list.clear();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        System.out.println(list.get(3));
        System.out.println(list.get(4));
        printSeparator();

        //Three
        printTestName("Set");
        list.set(1, "2 - edited");
        list.set(3, "4 - edited");

        System.out.println(Arrays.toString(list.toArray()));
        printSeparator();

        //Four
        printTestName("Contains");
        System.out.println(list.contains("3"));
        System.out.println(list.contains("2 - edited"));
        System.out.println(list.contains("pesho"));
        printSeparator();

        //Five
        printTestName("Remove");
        list.clear();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("3");
        list.add("3.5");
        list.add("4");
        list.add("5");

        list.remove("3");
        list.remove(0);
        list.remove("4");
        list.remove(3);

        System.out.println(Arrays.toString(list.toArray()));
        System.out.println(list.getSize());
        printSeparator();

        //Five
        printTestName("Iterator");
        list.clear();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String> iter = list.iterator();

        while (iter.hasNext()) {
            String current = iter.next();
            System.out.println("Current - " + current);

            if (current.equals("2") || current.equals("4")) {
                iter.remove();

                System.out.println("Removed - " + current);
            }
        }

        System.out.println();
        System.out.println(list);
        System.out.println();
        System.out.println(Arrays.toString(list.toArray()));
        printSeparator();
    }

    private static void printTestName(String name) {
        System.out.printf("Test - %s%n+++++++++++++++++++++++++++++++%n", name);
    }

    private static void printSeparator() {
        System.out.printf("-------------------------------%n%n");
    }
}
