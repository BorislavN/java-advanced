package streams_files_dirs.sandbox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

//Speed comparison between different writers
//On average the BufferedWriter is fastest
public class WritersSpeedComparison {
    public static void main(String[] args) {
        Path currentLectureFolder = Path.of("src/streams_files_dirs/exercises");

        Path inputPath = currentLectureFolder.resolve("resources/testInput.txt");
        Path outputPathOne = currentLectureFolder.resolve("resources/outputOne.txt");
        Path outputPathTwo = currentLectureFolder.resolve("resources/outputTwo.txt");
        Path outputPathThree = currentLectureFolder.resolve("resources/outputThree.txt");

        StringBuilder output = new StringBuilder();

        try (
                PrintWriter printWriter = new PrintWriter(outputPathTwo.toFile());
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPathThree.toString()));
                FileWriter fileWriter = new FileWriter(outputPathOne.toFile())
        ) {
            List<String> lines = Files.readAllLines(inputPath);

            //FileWriter
            Instant start = Instant.now();

            for (String line : lines) {
                fileWriter.write(line.toUpperCase());
                fileWriter.append(System.lineSeparator());
            }

            fileWriter.flush();
            Instant end = Instant.now();

            output.append(String.format("FileWriter: %d%n%n", Duration.between(start, end).toMillis()));
            Thread.sleep(3000);

            //PrintWriter
            start = Instant.now();

            for (String s : lines) {
                printWriter.println(s.toUpperCase());
            }

            printWriter.flush();
            end = Instant.now();

            output.append(String.format("PrintWriter: %d%n%n", Duration.between(start, end).toMillis()));
            Thread.sleep(3000);

            //BufferedWriter
            start = Instant.now();

            for (String line : lines) {
                bufferedWriter.write(line.toUpperCase());
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            end = Instant.now();

            output.append(String.format("BufferedWriter: %d%n", Duration.between(start, end).toMillis()));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(output);
    }
}