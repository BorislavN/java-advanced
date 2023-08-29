package streams_files_dirs.exercises.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SumLines {
    public static void main(String[] args) {
        Path path = Path.of("src/streams_files_dirs/exercises/resources/input.txt");
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String input;

            while (null != (input = reader.readLine())) {
                output.append(input.chars().sum());
                output.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(output);
    }
}