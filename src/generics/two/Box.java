package generics.two;

public class Box<V> {
    private final V value;

    public Box(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.value.getClass().getName(), this.value);
    }
}
