package defining_classes.four;

import java.util.ArrayList;
import java.util.List;

public class Tire {
    private final double pressure;
    private final int age;

    public Tire(double pressure, int age) {
        this.pressure = pressure;
        this.age = age;
    }

    public boolean isDeflated(){
        return this.pressure<1;
    }

    public static List<Tire> createTires(String[] data) {
        List<Tire> tires = new ArrayList<>();

        for (int index = 0; index < data.length; index += 2) {
            double pressure = Double.parseDouble(data[index]);
            int age = Integer.parseInt(data[index + 1]);

            tires.add(new Tire(pressure, age));
        }

        return tires;
    }
}
