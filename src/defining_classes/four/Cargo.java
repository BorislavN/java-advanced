package defining_classes.four;

public class Cargo {
    private final int weight;
    private final  String type;

    public Cargo(int weight, String type) {
        this.weight = weight;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
