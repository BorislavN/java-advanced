package iterators_and_comperators.petclinics;

public class Pet {
    private final String name;
    private final int age;
    private final String kind;

    public Pet(String name, int age, String kind) {
        this.name = name;
        this.age = age;
        this.kind = kind;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", this.name, this.age, this.kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        return this.name.equals(pet.getName());
    }

    @Override
    public int hashCode() {
        return 31 * this.name.hashCode();
    }
}
