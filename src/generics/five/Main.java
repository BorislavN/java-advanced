package generics.five;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Box<Integer>> list;
        list = new ArrayList<>();

        int n = Integer.parseInt(reader.readLine());

        while (n-- > 0) {
            list.add(new Box<>(Integer.parseInt(reader.readLine())));
        }

        int[] indexes = Arrays.stream(reader.readLine()
                        .split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();


        list.forEach(System.out::println);
    }
}
