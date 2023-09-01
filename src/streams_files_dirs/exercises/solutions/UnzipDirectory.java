package streams_files_dirs.exercises.solutions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//This class is dedicated to unzipping the zip created by the other ZipDirectory.java
//It accepts only regular files inside the input archive
public class UnzipDirectory {
    public static void main(String[] args) {
        Path in = Path.of("src/streams_files_dirs/exercises/resources/outputCompressed.zip");
        Path out = Path.of("src/streams_files_dirs/exercises/resources/unzipped");

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(in))) {
            if (!Files.exists(out)) {
                Files.createDirectory(out);
            }

            ZipEntry entry;

            while ((entry = inputStream.getNextEntry()) != null) {
                File current = createFile(out.toFile(), entry);

                if (current.isDirectory()) {
                    throw new IllegalArgumentException("Expecting file but found a directory!");
                }

                FileOutputStream outputStream = new FileOutputStream(current);
                byte[] buffer = new byte[1024];
                int dataLength;

                while ((dataLength = inputStream.read(buffer)) >= 0) {
                    outputStream.write(buffer, 0, dataLength);
                }

                outputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This should protect against Zip Slip attack
    //The filepath is checked before returning the new file
    //If the path is malicious an Exception will be thrown
    private static File createFile(File destination, ZipEntry entry) throws IOException {
        File current = new File(destination, entry.getName());

        String destinationDir = destination.getCanonicalPath();
        String currentDir = current.getCanonicalPath();

        if (!currentDir.startsWith(destinationDir + File.separator)) {
            throw new IOException("Entry destination is outside of target folder!");
        }

        return current;
    }
}