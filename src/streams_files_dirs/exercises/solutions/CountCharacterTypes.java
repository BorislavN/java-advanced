package streams_files_dirs.exercises.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;

public class CountCharacterTypes {
    public static void main(String[] args) {
        //This style of slashes also works
        Path input = Path.of("src\\streams_files_dirs\\exercises\\resources\\input.txt");
        Path output = Path.of("src\\streams_files_dirs\\exercises\\resources\\output.txt");

        try (
                BufferedReader reader = Files.newBufferedReader(input);
                PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output, CREATE));
        ) {
            String vowels = "aeiou";
            String punctuation = ".!?,";

            int otherCount = 0;
            int vowelsCount = 0;
            int punctuationCount = 0;

            for (String line : reader.lines().toArray(String[]::new)) {
                for (int index = 0; index < line.length(); index++) {
                    String current = String.valueOf(line.charAt(index));

                    if (vowels.contains(current)) {
                        vowelsCount++;
                        continue;
                    }

                    if (punctuation.contains(current)) {
                        punctuationCount++;
                        continue;
                    }

                    if (!Character.isWhitespace(line.charAt(index))) {
                        otherCount++;
                    }
                }
            }

            writer.printf("Vowels: %d%n", vowelsCount);
            writer.printf("Other symbols: %d%n", otherCount);
            writer.printf("Punctuation: %d%n", punctuationCount);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}