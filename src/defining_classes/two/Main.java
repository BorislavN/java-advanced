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
            Employee employee = new Employee(data);

            departments.putIfAbsent(data[3], new Department(data[3]));
            departments.get(data[3]).add(employee);
        }

        Optional<Department> bestDepartment = departments.values().stream()
                .max(Comparator.comparingDouble(Department::getAverage));

        bestDepartment.ifPresent(System.out::println);
    }
}
