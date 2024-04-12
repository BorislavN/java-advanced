package defining_classes.nine;

public class Cymric extends Cat {
    private final double furLength;

    public Cymric(String name, double furLength) {
        super(name);
        this.furLength = furLength;
    }

    public double getFurLength() {
        return this.furLength;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", super.toString(), this.furLength);
    }
}
