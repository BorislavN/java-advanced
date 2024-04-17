package generics.five;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Box<String>> list = new ArrayList<>();

        int n = Integer.parseInt(reader.readLine());

        while (n-- > 0) {
            list.add(new Box<>((reader.readLine())));
        }

        String filter = reader.readLine();

        Predicate<String> comparison = (value) -> value.compareTo(filter) > 0;

        System.out.println(list.stream().filter(e -> e.isGrater(comparison)).count());
    }
}
