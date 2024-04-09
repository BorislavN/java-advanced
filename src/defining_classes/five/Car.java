package defining_classes.five;

public class Car {
    private final String model;
    private final Engine engine;
    private String weight;
    private String color;

    public Car(String model, Engine engine, String[]args) {
        this.model = model;
        this.engine = engine;
        this.weight = "n/a";
        this.color = "n/a";

        if (args.length == 2) {
            this.weight = args[0];
            this.color = args[1];
        }

        if (args.length == 1) {
            if (Character.isAlphabetic(args[0].charAt(0))) {
                this.color = args[0];
            } else {
                this.weight = args[0];
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s:%n%s%nWeight: %s%nColor: %s"
                , this.model
                , this.engine
                , this.weight
                , this.color);
    }
}
