package iterators_and_comperators.petclinics;

public class Clinic {
    private final String name;
    private final Pet[] rooms;
    private final int centerIndex;

    public Clinic(String name, int numberOfRooms) {
        if (numberOfRooms % 2 == 0) {
            throw new IllegalArgumentException("Invalid Operation!");
        }

        this.name = name;
        this.rooms = new Pet[numberOfRooms];
        this.centerIndex = this.rooms.length / 2;
    }

    public String getName() {
        return this.name;
    }

    public boolean add(Pet pet) {
        Pet center = this.rooms[this.centerIndex];

        if (center == null) {
            this.rooms[this.centerIndex] = pet;

            return true;
        }

        int offset = 1;

        while (offset <= this.centerIndex) {
            int toTheLeft = this.centerIndex - offset;
            int toTheRight = this.centerIndex + offset;

            boolean result = this.putInRoom(toTheLeft, pet);

            if (!result) {
                result = this.putInRoom(toTheRight, pet);
            }

            if (result) {
                return true;
            }

            offset++;
        }

        return false;
    }

    public boolean release() {
        boolean result = this.resetRoom(this.centerIndex, this.rooms.length);

        if (!result) {
            result = this.resetRoom(0, this.centerIndex);
        }

        return result;
    }

    public boolean hasRooms() {
        for (Pet room : this.rooms) {
            if (room == null) {
                return true;
            }
        }

        return false;
    }

    public void print(int room) {
        Pet current = this.rooms[room];

        if (current != null) {
            System.out.println(current);
        } else {
            System.out.println("Room empty");
        }
    }

    public void print() {
        for (int i = 0; i < this.rooms.length; i++) {
            this.print(i);
        }
    }

    private boolean putInRoom(int index, Pet pet) {
        if ((index >= 0 && index < this.rooms.length) && this.rooms[index] == null) {
            this.rooms[index] = pet;

            return true;
        }

        return false;
    }

    private boolean resetRoom(int start, int end) {
        for (int i = start; i < end; i++) {
            if (this.rooms[i] != null) {
                this.rooms[i] = null;

                return true;
            }
        }

        return false;
    }
}