package defining_classes.five;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Engine> engines = new HashMap<>();
        List<Car> cars = new ArrayList<>();


        for (int count = 0; count < 2; count++) {
            int n = Integer.parseInt(reader.readLine());

            while (n-- > 0) {
                String[] data = reader.readLine().split("\\s+");

                if (count == 0) {
                    engines.putIfAbsent(data[0], new Engine(data));
                }

                if (count == 1) {
                    Engine engine = engines.get(data[1]);

                    cars.add(new Car(data[0], engine, Arrays.copyOfRange(data, 2, data.length)));
                }
            }
        }

        cars.forEach(System.out::println);
    }
}
