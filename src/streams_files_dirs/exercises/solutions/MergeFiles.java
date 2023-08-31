package streams_files_dirs.exercises.solutions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class MergeFiles {
    public static void main(String[] args) {
        Path inOne = Path.of("src/streams_files_dirs/exercises/resources/inputOne.txt");
        Path inTwo = Path.of("src/streams_files_dirs/exercises/resources/inputTwo.txt");
        Path out = Path.of("src/streams_files_dirs/exercises/resources/output.txt");

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(out, CREATE, TRUNCATE_EXISTING))) {
            Stream.concat(Files.lines(inOne), Files.lines(inTwo)).forEach(writer::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
