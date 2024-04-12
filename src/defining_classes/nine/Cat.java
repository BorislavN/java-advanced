package defining_classes.nine;

public class Cat {
    private final String name;

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), this.name);
    }
}
