package defining_classes.six;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trainer {
    private final String name;
    private int badges;
    private final List<Pokemon> pokemon;

    public Trainer(String name) {
        this.name = name;
        this.badges = 0;
        this.pokemon = new ArrayList<>();
    }

    public int getBadges() {
        return this.badges;
    }

    public void catchPokemon(Pokemon pokemon) {
        this.pokemon.add(pokemon);
    }

    public void fight(String element) {
        if (this.hasElement(element)) {
            this.badges++;

            return;
        }


        Iterator<Pokemon> iterator = this.pokemon.iterator();

        while (iterator.hasNext()) {
            Pokemon current = iterator.next();

            current.takeDamage();

            if (current.isDone()) {
                iterator.remove();
            }
        }
    }


    private boolean hasElement(String element) {
        return this.pokemon.stream().anyMatch(e -> element.equals(e.getElement()));
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", this.name, this.badges, this.pokemon.size());
    }
}
