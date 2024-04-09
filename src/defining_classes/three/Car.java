package defining_classes.three;

public class Car {
    private String model;
    private double fuel;
    private double fuelCost;
    private int distance;

    public Car(String model, double fuel, double fuelCost) {
        this.model = model;
        this.fuel = fuel;
        this.fuelCost = fuelCost;
        this.distance = 0;
    }

    public void drive(int kilometers) {
        double requiredFuel = this.fuelCost * kilometers;

        if (requiredFuel > this.fuel) {
            System.out.println("Insufficient fuel for the drive");

            return;
        }

        this.fuel -= requiredFuel;
        this.distance += kilometers;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f %d", this.model, this.fuel, this.distance);
    }
}