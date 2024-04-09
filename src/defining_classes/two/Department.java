package defining_classes.two;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Department {
    private final String name;
    private final List<Employee> employees;

    public Department(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }

    public double getAverage() {
        return this.employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
    }

    public void add(Employee employee) {
        this.employees.add(employee);
    }

    @Override
    public String toString() {
        String list = this.employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .map(Employee::toString)
                .collect(Collectors.joining(System.lineSeparator()));

        return String.format("Highest Average Salary: %s%n%s", this.name, list);
    }
}
