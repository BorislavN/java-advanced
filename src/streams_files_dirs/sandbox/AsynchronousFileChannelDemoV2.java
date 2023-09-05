package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Executors;

import static java.nio.file.StandardOpenOption.*;

//Variant with  CompletionHandler<>
public class AsynchronousFileChannelDemoV2 {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync.txt");

        try (AsynchronousFileChannel outChannel = AsynchronousFileChannel.open(
                copyPath,
                Set.of(CREATE, WRITE, TRUNCATE_EXISTING),
                Executors.newFixedThreadPool(5)
        )) {
            for (String line : Files.readAllLines(filePath)) {
//                Thread.sleep(0,1);

                WriteHandler handler = new WriteHandler();
                ByteBuffer temp = ByteBuffer.wrap(String.format("%s%n", line).getBytes());
                outChannel.write(temp, outChannel.size(), line, handler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class WriteHandler implements CompletionHandler<Integer, String> {

    @Override
    public void completed(Integer result, String attachment) {
        System.out.printf("%s: writing %d bytes - line: %s...%n", Thread.currentThread().getName(), result, attachment);
    }

    @Override
    public void failed(Throwable exc, String attachment) {
        throw new RuntimeException("Error occurred while writing line: " + attachment);
    }
}