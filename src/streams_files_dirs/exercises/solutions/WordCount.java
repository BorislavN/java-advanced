package streams_files_dirs.exercises.solutions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

//Matching is not case-insensitive
//Can be if we use toLowerCase() on the lines in the stream
//but the keys in the map will become also lowe-case
public class WordCount {
    public static void main(String[] args) {
        Path inOne = Path.of("src/streams_files_dirs/exercises/resources/words.txt");
        Path inTwo = Path.of("src/streams_files_dirs/exercises/resources/text.txt");
        Path out = Path.of("src/streams_files_dirs/exercises/resources/output.txt");

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(out, TRUNCATE_EXISTING, CREATE))) {
            List<String> words = Files.readAllLines(inOne)
                    .stream()
                    .map(e -> e.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .toList();

            Map<String, Integer> countMap = Files.readAllLines(inTwo)
                    .stream()
                    .map(e -> e.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .filter(words::contains)
                    .collect(Collectors.toMap(Function.identity(), (v) -> 1, (o, n) -> o + 1, HashMap::new));

            countMap.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                    .forEach((e) -> writer.printf("%s - %d%n", e.getKey(), e.getValue()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}