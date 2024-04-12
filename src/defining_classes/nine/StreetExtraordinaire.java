package defining_classes.nine;

public class StreetExtraordinaire extends Cat {
    private final double decibelsOfMeows;

    public StreetExtraordinaire(String name, double decibelsOfMeows) {
        super(name);
        this.decibelsOfMeows = decibelsOfMeows;
    }

    public double getDecibelsOfMeows() {
        return this.decibelsOfMeows;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", super.toString(), this.decibelsOfMeows);
    }
}
