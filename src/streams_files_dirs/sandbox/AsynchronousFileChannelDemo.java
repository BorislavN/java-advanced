package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.READ;

//Variant with Future<>
public class AsynchronousFileChannelDemo {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync.txt");

        try (AsynchronousFileChannel inChannel = AsynchronousFileChannel.open(filePath, READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            long position = 0;

            FileLock fileLock = null;

            //Waits for a shared lock on the file
            //A shared lock permits read operations, but not writing
            //FileLocks are useful for preventing other programs from using the file
            //(depending on the system, some treat locks as a warning, not as a mandatory rule)
            while (fileLock == null) {
                fileLock = inChannel.tryLock(0, Long.MAX_VALUE, true);
            }

            while (position < inChannel.size()) {
                Future<Integer> result = inChannel.read(buffer, position);

                while (!result.isDone()) {
                    System.out.println("Waiting....");
                }

                position += result.get();

                buffer.flip();

                System.out.println(Thread.currentThread().getName());
                System.out.println(UTF_8.decode(buffer));
                System.out.println();

                buffer.clear();
            }

            fileLock.release();

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}