package exam.sharkHaunt;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Basin {
    private String name;
    private int capacity;
    private final List<Shark> sharks;

    public Basin(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.sharks = new ArrayList<>(capacity);
    }

    public String getName() {
        return this.name;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addShark(Shark shark) {
        if (this.sharks.size() < this.capacity) {
            this.sharks.add(shark);
        } else {
            System.out.println("This basin is at full capacity!");
        }
    }

    public boolean removeShark(String kind) {
        return this.sharks.removeIf(s -> kind.equals(s.getKind()));
    }

    public Shark getLargestShark() {
        return this.sharks.stream().max(Comparator.comparing(Shark::getLength)).orElse(null);
    }

    public Shark getShark(String kind) {
        return this.sharks.stream().filter(s -> kind.equals(s.getKind())).findFirst().orElse(null);
    }

    public int getCount() {
        return this.sharks.size();
    }

    public int getAverageLength() {
        return (int) this.sharks.stream().mapToDouble(Shark::getLength).average().orElse(0);
    }

    public String report() {
        StringBuilder output = new StringBuilder();

        output.append("Sharks in ").append(this.name).append(":");
        output.append(System.lineSeparator());

        this.sharks.forEach(s -> output.append(s).append(System.lineSeparator()));

        return output.toString().trim();
    }
}
