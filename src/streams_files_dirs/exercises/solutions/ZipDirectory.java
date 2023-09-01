package streams_files_dirs.exercises.solutions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class ZipDirectory {
    public static void main(String[] args) {
        Path dir = Path.of("src/streams_files_dirs/exercises/resources/Exercises Resources");
        Path out = Path.of("src/streams_files_dirs/exercises/resources/outputCompressed.zip");

        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(out, TRUNCATE_EXISTING, CREATE));
        ) {
            File directory = dir.toFile();

            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("Provided path is not a directory.");
            }

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                //skips directories and already zip-ed files
                if (!file.isDirectory() && !file.getName().endsWith("zip")) {
                    zipOutputStream.putNextEntry(new ZipEntry( file.getName()));

                    FileInputStream inputStream = new FileInputStream(file);

                    byte[] buffer = new byte[1024];
                    int dataLength;

                    while ((dataLength = inputStream.read(buffer)) >= 0) {
                        zipOutputStream.write(buffer, 0, dataLength);
                    }

                    zipOutputStream.closeEntry();
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}