package generics.five;

public class Box<V extends Comparable<V>> {
    private final V value;

    public Box(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.value.getClass().getName(), this.value);
    }

    public boolean isGrater(V filter) {
        return this.value.compareTo(filter) > 0;
    }
}
