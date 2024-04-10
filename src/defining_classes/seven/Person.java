package defining_classes.seven;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Person {
    private final String name;
    private Company company;
    private Car car;
    private final List<Pokemon> pokemon;
    private final List<FamilyMember> parents;
    private final List<FamilyMember> children;

    public Person(String name) {
        this.name = name;
        this.company = null;
        this.car = null;
        this.pokemon = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void addPokemon(Pokemon pokemon) {
        this.pokemon.add(pokemon);
    }

    public void addParent(FamilyMember parent) {
        this.parents.add(parent);
    }

    public void addChild(FamilyMember child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        Consumer<String> appendLabel = (label) -> {
            output.append(label);
            output.append(System.lineSeparator());
        };

        Consumer<Object> appendData = (obj) -> {
            if (obj != null) {
                output.append(obj);
                output.append(System.lineSeparator());
            }
        };

        output.append(this.name).append(System.lineSeparator());

        appendLabel.accept("Company:");
        appendData.accept(this.company);

        appendLabel.accept("Car:");
        appendData.accept(this.car);

        appendLabel.accept("Pokemon:");

        if (!this.pokemon.isEmpty()) {
            this.pokemon.forEach(appendData);
        }

        appendLabel.accept("Parents:");

        if (!this.parents.isEmpty()) {
            this.parents.forEach(appendData);
        }

        appendLabel.accept("Children:");

        if (!this.children.isEmpty()) {
            this.children.forEach(appendData);
        }

        return output.toString().trim();
    }
}
