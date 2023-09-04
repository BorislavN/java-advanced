package streams_files_dirs.sandbox;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class WatchDirDemo {
    public static void main(String[] args) {
        Path dir = Path.of("src/streams_files_dirs/exercises/resources/watched");

        if (dir.toFile().mkdir()) {
            System.out.println("Directory /watched/ was created.");
        }

        Thread watcher = new Thread(watchHandler(dir));
        Thread thread1 = new Thread(fileHandler(dir, "one.txt", 1000));
        Thread thread2 = new Thread(fileHandler(dir, "two.txt", 2000));

        watcher.start();
        thread1.start();
        thread2.start();
    }

    private static Runnable watchHandler(Path path) {
        return () -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                path.register(watchService, ENTRY_MODIFY);

                WatchKey key;

                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        System.out.printf("Event: %s - File: %s%n", event.kind(), event.context());
                    }
                    key.reset();
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Runnable fileHandler(Path path, String filename, int ms) {
        return () -> {
            int lines = 100;


            try {
                while ((lines -= 10) >= 0) {
                    Thread.sleep(ms);

                    //Each time we create the PrintWriter, so we can give the WatchService a chance to register the changes
                    //If we don't the file is blocked, because the writer is using it
                    //Alternatively we can get the writer out of the loop and just wait for thread to finish its job
                    PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path.resolve(filename)));

                    writer.printf("Thread: %s - working on file %s.txt, completed: %d%%%n"
                            , Thread.currentThread().getName()
                            , filename
                            , 100 - lines);

                    writer.flush();
                    writer.close();
                }

            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}