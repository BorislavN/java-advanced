package functional_programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReverseAndExclude {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String data = scanner.nextLine();
        int number = Integer.parseInt(scanner.nextLine());

        List<String> results = new ArrayList<>();

        Arrays.stream(data.split("\\s+"))
                .filter(v -> Integer.parseInt(v) % number != 0)
                .forEach(e -> results.add(0, e));

        System.out.println(String.join(" ", results));
    }
}