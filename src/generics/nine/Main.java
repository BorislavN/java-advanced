package generics.nine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        CustomList<String> list = new CustomList<>();

        String input;

        while (!"END".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            switch (data[0]) {
                case "Add":
                    list.add(data[1]);

                    break;

                case "Remove":
                    int index = Integer.parseInt(data[1]);
                    list.remove(index);

                    break;

                case "Contains":
                    System.out.println(list.contains(data[1]));

                    break;

                case "Swap":
                    int one = Integer.parseInt(data[1]);
                    int two = Integer.parseInt(data[2]);
                    list.swap(one, two);

                    break;

                case "Greater":
                    System.out.println(list.countGreaterThan(data[1]));

                    break;

                case "Max":
                    System.out.println(list.max());

                    break;

                case "Min":
                    System.out.println(list.min());

                    break;

                case "Sort":
                    Sorter.sort(list);

                    break;

                case "Print":
                    list.forEach(System.out::println);

                    break;
            }
        }
    }
}