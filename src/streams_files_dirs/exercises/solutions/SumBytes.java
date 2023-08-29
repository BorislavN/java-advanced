package streams_files_dirs.exercises.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SumBytes {
    public static void main(String[] args) {
        Path currentLectureFolder = Path.of("src/streams_files_dirs/exercises");
        Path filePath = currentLectureFolder.resolve("resources/input.txt");
        long sum = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String input;

            while (null != (input = reader.readLine())) {
                sum += input.chars().sum();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(sum);
    }
}