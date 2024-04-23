package iterators_and_comperators.strategypattern;

public class Person {
    private final String name;
    private final int age;

    public Person(String[] data) {
        this.name = data[0];
        this.age = Integer.parseInt(data[1]);
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return String.format("%s %d", this.name, this.age);
    }
}
