package streams_files_dirs.exercises.solutions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AllCapitals {
    public static void main(String[] args) {
        Path currentLectureFolder = Path.of("src/streams_files_dirs/exercises");

        Path inputPath = currentLectureFolder.resolve("resources/input.txt");
        Path outputPath = currentLectureFolder.resolve("resources/output.txt");

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputPath.toFile())))) {
            List<String> lines = Files.readAllLines(inputPath);

            lines.forEach(line -> writer.println(line.toUpperCase()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
