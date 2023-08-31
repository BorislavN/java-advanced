package streams_files_dirs.exercises.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;

public class FolderSize {
    public static void main(String[] args) throws IOException {
        Path folderpath = Path.of("src/streams_files_dirs/exercises/resources/Exercises Resources");

        try (Stream<Path> paths = Files.walk(folderpath, MAX_VALUE)) {
            long size = paths.filter(p -> !p.equals(folderpath))
                    .mapToLong(e -> e.toFile().length())
                    .sum();

            System.out.printf("Folder size: %d%n",size);
        }
    }
}