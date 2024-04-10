package defining_classes.seven;

public class FamilyMember {
    private final String name;
    private final String birthday;

    public FamilyMember(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.name, this.birthday);
    }
}
