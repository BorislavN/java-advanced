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
import java.util.Scanner;

//Speed comparison between different writers
//On average the BufferedWriter is fastest
public class WritersSpeedComparison {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Path currentLectureFolder = Path.of("src/streams_files_dirs/exercises");

        Path inputPath = currentLectureFolder.resolve("resources/testInput.txt");
        Path outputPathOne = currentLectureFolder.resolve("resources/outputOne.txt");
        Path outputPathTwo = currentLectureFolder.resolve("resources/outputTwo.txt");
        Path outputPathThree = currentLectureFolder.resolve("resources/outputThree.txt");

        System.out.println("Enter number of loops:");
        int times = Integer.parseInt(scanner.nextLine());

        double fileWriterTotal = 0;
        double printWriterTotal = 0;
        double bufferedWriterTotal = 0;

        for (int loop = 0; loop < times; loop++) {
            try (
                    PrintWriter printWriter = new PrintWriter(outputPathTwo.toFile());
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPathThree.toString()));
                    FileWriter fileWriter = new FileWriter(outputPathOne.toFile())
            ) {
                List<String> lines = Files.readAllLines(inputPath);

                //FileWriter
                Instant start = Instant.now();

                for (String line : lines) {
                    fileWriter.write(line.toUpperCase() + " - WORK");
                    fileWriter.append(System.lineSeparator());
                }

                Instant end = Instant.now();

                fileWriterTotal += Duration.between(start, end).toMillis();

                //PrintWriter
                start = Instant.now();

                for (String s : lines) {
                    printWriter.println(s.toUpperCase() + " - WORK");
                }

                end = Instant.now();

                printWriterTotal += Duration.between(start, end).toMillis();

                //BufferedWriter
                start = Instant.now();

                for (String line : lines) {
                    bufferedWriter.write(line.toUpperCase() + " - WORK");
                    bufferedWriter.newLine();
                }

                end = Instant.now();

                bufferedWriterTotal += Duration.between(start, end).toMillis();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("FileWriter: %.2f%n%n", fileWriterTotal / times);
        System.out.printf("PrintWriter: %.2f%n%n", printWriterTotal / times);
        System.out.printf("BufferedWriter: %.2f%n", bufferedWriterTotal / times);
    }
}