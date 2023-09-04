package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

public class FileChannelDemo {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/data.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyData.txt");
        int filePathInitialSize = 442;

        try (
                FileChannel inChannel = FileChannel.open(filePath, READ, WRITE);
                FileChannel outChannel = FileChannel.open(copyPath, CREATE, WRITE, TRUNCATE_EXISTING);
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(256);

            if (inChannel.size() > filePathInitialSize) {
                inChannel.truncate(filePathInitialSize);//Resets the file to the initial length
            }

            while (inChannel.read(buffer) > 0) {//reads data in buffer
                buffer.flip();//returns to starting position
                outChannel.write(buffer);//writes the data from the buffer
                buffer.clear();//empties the buffer
            }

            //the channels are bidirectional - we write data to the same file we read from
            inChannel.write(ByteBuffer.wrap(String.format("%n%n Just ended Demo %n").getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
