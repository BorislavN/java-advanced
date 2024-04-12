package defining_classes.nine;

public class Siamese extends  Cat{
    private final double earSize;
    public Siamese(String name, double earSize) {
        super(name);
        this.earSize=earSize;
    }

    public double getEarSize() {
        return this.earSize;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", super.toString(), this.earSize);
    }
}
