package defining_classes.four;

import java.util.Arrays;
import java.util.List;

public class Car {
    private final String model;
    private final Engine engine;
    private final Cargo cargo;
    private final List<Tire> tires;

    public Car(String[] data) {
        this.model = data[0];

        int speed = Integer.parseInt(data[1]);
        int power = Integer.parseInt(data[2]);

        this.engine = new Engine(speed, power);

        int weight = Integer.parseInt(data[3]);

        this.cargo = new Cargo(weight, data[4]);

        this.tires = Tire.createTires(Arrays.copyOfRange(data, 5, data.length));
    }

    public boolean isFragile() {
        return "fragile".equals(this.cargo.getType()) && this.hasFlat();
    }

    public boolean isFlammable() {
        return "flamable".equals(this.cargo.getType()) && this.engine.isPowerful();
    }

    private boolean hasFlat() {
        for (Tire t : this.tires) {
            if (t.isDeflated()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return this.model;
    }
}