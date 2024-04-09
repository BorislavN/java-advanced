package defining_classes.four;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        List<Car> cars = new ArrayList<>();

        while (n-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            cars.add(new Car(data));
        }

        String filter = reader.readLine();

        Predicate<Car> condition = car -> "flamable".equals(filter) ? car.isFlammable() : car.isFragile();

        cars.stream().filter(condition).forEach(System.out::println);
    }
}
