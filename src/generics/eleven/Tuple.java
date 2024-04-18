package generics.eleven;

public class Tuple<F, S> {
    private F itemOne;
    private S itemTwo;

    public Tuple(F itemOne, S itemTwo) {
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    public F getItemOne() {
        return this.itemOne;
    }

    public void setItemOne(F itemOne) {
        this.itemOne = itemOne;
    }

    public S getItemTwo() {
        return this.itemTwo;
    }

    public void setItemTwo(S itemTwo) {
        this.itemTwo = itemTwo;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", this. itemOne, this.itemTwo);
    }
}
