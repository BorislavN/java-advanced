package iterators_and_comperators.listyiterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> data = Arrays.stream(reader.readLine().split("\\s+"))
                .skip(1).collect(Collectors.toList());

        ListyIterator iterator = new ListyIterator(data);

        String input;

        while (!"END".equals(input = reader.readLine())) {
            try {
                switch (input) {
                    case "HasNext":
                        System.out.println(iterator.hasNext());

                        break;

                    case "Move":
                        System.out.println(iterator.move());

                        break;

                    case "Print":
                        iterator.print();

                        break;

                    case "PrintAll":
                        StringBuilder output = new StringBuilder();

                        iterator.forEach(e -> output.append(e).append(" "));

                        System.out.println(output);

                        break;
                }
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}