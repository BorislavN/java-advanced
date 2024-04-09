package defining_classes.three;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Car> cars = new LinkedHashMap<>();

        int n = Integer.parseInt(reader.readLine());

        while (n-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            double fuel = Double.parseDouble(data[1]);
            double fuelCost = Double.parseDouble(data[2]);

            cars.putIfAbsent(data[0], new Car(data[0], fuel, fuelCost));
        }

        String command;

        while (!"End".equals(command = reader.readLine())) {
            String[] data = command.split("\\s+");

            int distance = Integer.parseInt(data[2]);

            Car current = cars.get(data[1]);

            if (current != null) {
                current.drive(distance);
            }
        }

        cars.values().forEach(System.out::println);
    }
}
