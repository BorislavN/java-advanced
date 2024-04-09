package defining_classes.four;

public class Engine {
    private final int speed;
    private final  int power;

    public Engine(int speed, int power) {
        this.speed = speed;
        this.power = power;
    }

    public boolean isPowerful(){
        return this.power>250;
    }
}
