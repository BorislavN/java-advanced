package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

//Variant with  CompletionHandler<>
//We just print what the current line is
//We wait on the end - to let the printf() finish
//Or we will be missing some output in th console, the file will be written correctly
//But the program will end before printf() finish
public class AsynchronousFileChannelDemoV2 {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync.txt");

        try (AsynchronousFileChannel outChannel = AsynchronousFileChannel.open(copyPath, TRUNCATE_EXISTING, CREATE, WRITE)) {
            WriteHandler handler = new WriteHandler();

            for (String line : Files.readAllLines(filePath)) {
                ByteBuffer temp = ByteBuffer.wrap(String.format("%s%n", line).getBytes());

                outChannel.write(temp, outChannel.size(), line, handler);
            }

            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class WriteHandler implements CompletionHandler<Integer, String> {
    @Override
    public void completed(Integer result, String attachment) {
        System.out.printf("%s: writing %d bytes...%n", Thread.currentThread().getName(), result);
    }

    @Override
    public void failed(Throwable exc, String attachment) {
        throw new RuntimeException("Error occurred while writing line: " + attachment);
    }
}