package streams_files_dirs.sandbox;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class RandomAccessFileDemo {
    public static void main(String[] args) {
        Path path = Path.of("src/streams_files_dirs/exercises/resources/sandbox/sharedRandomFile.txt");

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw")) {
            Map<Long, String> allLines = new TreeMap<>();
            String line;

            resetFile(path);

            while ((line = file.readLine()) != null) {
                //-2 because the new line in windows is \r\n
                long startIndex = file.getFilePointer() - line.length() - 2;

                //The map contains the lines and the line-start indexes
                allLines.put(startIndex, line);
            }

            //The file is a large buffer loaded in memory, if we write content in the middle it is overwritten
            for (Long key : allLines.keySet()) {
                if (allLines.get(key).endsWith("7") || allLines.get(key).endsWith("14")) {
                    String newLine = "/EDITED - LINES\\";

                    file.seek(key);
                    file.writeBytes(newLine);
                }

                if (allLines.get(key).isBlank()) {
                    file.seek(key);
                    file.writeBytes(String.format("Overwrite ;D%n"));
                }
            }

            file.seek(file.length());
            file.writeBytes(String.format("%nNew Line Baby%n"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Opens the file, resets it, closes it - so the changes are written to memory
    private static void resetFile(Path path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
        file.setLength(0);
        file.writeBytes(String.format("RandomAccessFile - Line 1%nRandomAccessFile - Line 2%nRandomAccessFile - Line 3%nRandomAccessFile - Line 4%nRandomAccessFile - Line 5%nRandomAccessFile - Line 6%nRandomAccessFile - Line 7%nRandomAccessFile - Line 8%nRandomAccessFile - Line 9%nRandomAccessFile - Line 10%n%nRandomAccessFile - Line 12%nRandomAccessFile - Line 13%nRandomAccessFile - Line 14%nRandomAccessFile - Line 15%nRandomAccessFile - Line 16"));
        file.close();
    }
}