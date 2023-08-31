package streams_files_dirs.exercises.solutions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class LineNumbers {
    public static void main(String[] args) {
        Path in = Path.of("src/streams_files_dirs/exercises/resources/inputLineNumbers.txt");
        Path out = Path.of("src/streams_files_dirs/exercises/resources/output.txt");

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(out, TRUNCATE_EXISTING,CREATE))) {
            List<String> lines = Files.readAllLines(in);

            for (int index = 0; index < lines.size(); index++) {
                writer.printf("%d. %s%n", index + 1, lines.get(index));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}