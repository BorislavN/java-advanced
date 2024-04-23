package iterators_and_comperators.comparingobjects;

public class Person implements Comparable<Person> {
    private final String name;
    private final int age;
    private final String town;

    public Person(String[] data) {
        this.name = data[0];
        this.age = Integer.parseInt(data[1]);
        this.town = data[2];
    }

    @Override
    public int compareTo(Person other) {
        int result = this.name.compareTo(other.name);

        if (result == 0) {
            result = Integer.compare(this.age, other.age);
        }

        if (result == 0) {
            result = this.town.compareTo(other.town);
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
