package defining_classes.five;

public class Engine {
    private final String model;
    private final String power;
    private String displacement;
    private String efficiency;

    public Engine(String[] args) {
        this.model = args[0];
        this.power = args[1];
        this.displacement = "n/a";
        this.efficiency = "n/a";

        if (args.length == 4) {
            this.displacement = args[2];
            this.efficiency = args[3];
        }

        if (args.length == 3) {
            if (Character.isDigit(args[2].charAt(0))) {
                this.displacement = args[2];
            } else {
                this.efficiency = args[2];
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s:%nPower: %s%nDisplacement: %s%nEfficiency: %s"
                , this.model
                , this.power
                , this.displacement
                , this.efficiency);
    }
}
