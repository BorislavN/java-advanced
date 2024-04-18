package generics.eleven;

public class Threeuple<F, S, T> extends Tuple<F, S> {
    private T itemThree;

    public Threeuple(F itemOne, S itemTwo, T itemThree) {
        super(itemOne, itemTwo);
        this.itemThree = itemThree;
    }

    public T getItemThree() {
        return this.itemThree;
    }

    public void setItemThree(T itemThree) {
        this.itemThree = itemThree;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", super.toString(), this.itemThree);
    }
}
