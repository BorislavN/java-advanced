package defining_classes.two;

public class Employee {
    private final String name;
    private final double salary;
    private final String position;
    private final String department;
    private String email;
    private int age;

    public Employee(String[] data) {
        this.email = "n/a";
        this.age = -1;

        this.name = data[0];
        this.salary = Double.parseDouble(data[1]);
        this.position = data[2];
        this.department = data[3];

        if (data.length == 6) {
            this.email = data[4];
            this.age = Integer.parseInt(data[5]);
        }

        if (data.length == 5) {
            if (data[4].contains("@")) {
                this.email = data[4];
            } else {
                this.age = Integer.parseInt(data[4]);
            }
        }
    }

    public double getSalary() {
        return this.salary;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f %s %d", this.name, this.salary, this.email, this.age);
    }
}
