package defining_classes.six;

public class Pokemon {
    private final String name;
    private final String element;
    private  int health;

    public Pokemon(String name, String element, int health) {
        this.name = name;
        this.element = element;
        this.health = health;
    }

    public String getElement() {
        return this.element;
    }

    public void takeDamage() {
        this.health -= 10;
    }

    public boolean isDone() {
        return this.health <= 0;
    }
}
