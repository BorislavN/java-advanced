package defining_classes.two;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        Map<String, Department> departments = new HashMap<>();

        while (n-- > 0) {
            String[] data = reader.readLine().split("\\s+");
            Employee employee;

            double salary = Double.parseDouble(data[1]);
            int age = -1;

            switch (data.length) {
                case 4:
                    employee = new Employee(data[0], salary, data[2], data[3]);
                    break;
                case 5:
                    if (data[4].contains("@")) {
                        employee = new Employee(data[0], salary, data[2], data[3], data[4]);

                        break;
                    }

                    age = Integer.parseInt(data[4]);
                    employee = new Employee(data[0], salary, data[2], data[3], age);

                    break;
                case 6:
                    age = Integer.parseInt(data[5]);
                    employee = new Employee(data[0], salary, data[2], data[3], data[4], age);

                    break;

                default:
                    throw new IllegalArgumentException("Wrong number of arguments!");
            }

            departments.putIfAbsent(data[3], new Department(data[3]));
            departments.get(data[3]).add(employee);
        }

        Optional<Map.Entry<String, Department>> best = departments.entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue().getAverage()));

        best.ifPresent(entry -> {
            System.out.printf("Highest Average Salary: %s%n", entry.getKey());
            System.out.println(entry.getValue());
        });
    }
}
