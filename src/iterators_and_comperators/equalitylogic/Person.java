package iterators_and_comperators.equalitylogic;

public class Person implements Comparable<Person> {
    private final String name;
    private final int age;

    public Person(String[] data) {
        this.name = data[0];
        this.age = Integer.parseInt(data[1]);
    }


    @Override
    public int compareTo(Person o) {
        int result = this.name.compareTo(o.name);

        if (result == 0) {
            result = Integer.compare(this.age, o.age);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (this.age != person.age) return false;

        return this.name.equals(person.name);
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.age;

        return result;
    }
}
