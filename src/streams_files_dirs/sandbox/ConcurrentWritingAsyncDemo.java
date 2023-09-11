package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class ConcurrentWritingAsyncDemo {
    public static void main(String[] args) {
        Path path = Path.of("src/streams_files_dirs/exercises/resources/sandbox/sharedFileAsync.txt");

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, WRITE, CREATE, TRUNCATE_EXISTING)) {
            createThread("Thread-1", channel).start();
            createThread("Thread-2", channel).start();
            createThread("Thread-3", channel).start();

            //waiting for the writes to end
            //if we don't the code exits the try block and closes the channel before the writes are done
            Thread.sleep(3000);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> generateText() {
        String name = Thread.currentThread().getName();

        return Stream.iterate(1, n -> n <= 15, n -> n + 1)
                .map(n -> String.format("%s - Working on Line %d%n", name, n))
                .toList();
    }

    private static Thread createThread(String name, AsynchronousFileChannel channel) {
        return new Thread(() -> {
            List<String> text = generateText();

            //without synchronization, we cant guarantee the consistency of the result
            //Because the AsyncFileChannel does bot have an internal pointer
            //we set the position manually, but because the concurrent access some lines get overwritten
            try {
                for (String line : text) {
                    synchronized (channel) {
                        channel.write(ByteBuffer.wrap(line.getBytes()), channel.size());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, name);
    }
}