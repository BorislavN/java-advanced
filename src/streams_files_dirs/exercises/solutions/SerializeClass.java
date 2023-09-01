package streams_files_dirs.exercises.solutions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class SerializeClass {
    public static void main(String[] args) {
        Path out = Path.of("src/streams_files_dirs/exercises/resources/outputClass.ser");

        try (
                ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(out, TRUNCATE_EXISTING, CREATE));
                ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(out));
        ) {
            Course current = new Course("JavaAdvanced", 300);
            outputStream.writeObject(current);
            outputStream.flush();

            Course loaded = (Course) inputStream.readObject();

            System.out.println("Original: " + current);
            System.out.println("Loaded: " + loaded);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Course implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String name;
        private int studentCount;

        public Course(String name, int studentCount) {
            this.name = name;
            this.studentCount = studentCount;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStudentCount() {
            return this.studentCount;
        }

        public void setStudentCount(int studentCount) {
            this.studentCount = studentCount;
        }

        @Override
        public String toString() {
            return String.format("Course: %s Enrolled: %d", this.name, this.studentCount);
        }
    }
}
