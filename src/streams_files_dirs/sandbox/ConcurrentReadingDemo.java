package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConcurrentReadingDemo {
    public static void main(String[] args) {
        Path path = Path.of("src/streams_files_dirs/exercises/resources/sandbox/sharedFile.txt");

        try (FileChannel channel = FileChannel.open(path)) {
            long[] ranges = getRanges(channel);
            int bufferSize = Math.toIntExact(ranges[1]);

            createThread("Thread-1", channel, bufferSize, ranges[0]).start();
            createThread("Thread-2", channel, bufferSize, ranges[1]).start();
            createThread("Thread-3", channel, bufferSize, ranges[2]).start();

            //waiting for the reads to end
            //if we don't the code exits the try block and closes the channel
            Thread.sleep(3000);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Thread createThread(String name, FileChannel channel, int size, long start) {
        return new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(size);

            try {
                int read = channel.read(buffer, start);

                if (read > 0) {
                    //we synchronize the writing to the console
                    synchronized (System.out) {
                        System.out.println();
                        System.out.println("||||||||||||||||||" + Thread.currentThread().getName() + "||||||||||||||||||");
                        System.out.println(UTF_8.decode(buffer.flip()));
                        System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, name);
    }

    private static long[] getRanges(FileChannel channel) throws IOException {
        long part = Math.round(channel.size() * 1.0 / 3);

        return new long[]{0L, part, part * 2};
    }
}