package defining_classes.eight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final List<Person> parents;
    private final List<Person> children;

    public Person(String firstName, String lastName, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"));
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addParent(Person parent) {
        this.parents.add(parent);
    }

    public void addChild(Person child) {
        this.children.add(child);
    }

    public String getNames(){
        return String.format("%s %s",this.firstName,this.lastName);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s"
                , this.firstName
                , this.lastName
                , this.birthday.format(DateTimeFormatter.ofPattern("d/M/yyyy")));
    }

    public static String getTree(Person person) {
        StringBuilder output = new StringBuilder();

        output.append(person).append(System.lineSeparator());

        output.append("Parents:").append(System.lineSeparator());
        person.parents.forEach(e -> output.append(e).append(System.lineSeparator()));

        output.append("Children:").append(System.lineSeparator());
        person.children.forEach(e -> output.append(e).append(System.lineSeparator()));

        return output.toString();
    }
}
