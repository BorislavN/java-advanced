package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

public class AsynchronousFileChannelDemo {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync.txt");

        try (AsynchronousFileChannel inChannel = AsynchronousFileChannel.open(filePath, READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            ReadHandler handler = new ReadHandler(copyPath);

            while (handler.getCurrentSize()<inChannel.size()){
                inChannel.read(buffer, handler.getCurrentSize(), buffer, handler);
            }

//            handler.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        private final AsynchronousFileChannel outChannel;
        private long size;

        public ReadHandler(Path path) throws IOException {
            this.outChannel = AsynchronousFileChannel.open(path, CREATE, WRITE);
            this.size = 0;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            this.outChannel.write(attachment, this.size);
            this.size += result;
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            throw new RuntimeException(exc.getMessage());
        }

        public void dispose() throws IOException {
            this.outChannel.close();
        }

        public long getCurrentSize() throws IOException {
            return this.size;
        }
    }
}