package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

//AsynchronousFileChannel demo with two channels
//One reads, the other writes
//I'm using a synchronized block to wait for a notify() call
//Then I'm sending the data form te buffer to be written
//Without the synchronization I was getting incorrect text in the output file
public class AsynchronousFileChannelDemoV3 {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync2.txt");

        try (
                AsynchronousFileChannel inChannel = AsynchronousFileChannel.open(filePath, READ);
                AsynchronousFileChannel outChannel = AsynchronousFileChannel.open(copyPath, TRUNCATE_EXISTING, WRITE, CREATE)
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            ReadHandler handler = new ReadHandler();

            while (inChannel.size() > outChannel.size()) {
                synchronized (handler.getLock()) {
                    inChannel.read(buffer, outChannel.size(), buffer, handler);

                    //Wait for the CompletionHandler to notify us
                    handler.getLock().wait();

                    //We copy the buffer, to avoid sharing it with the other channel
                    //If we don't, we enter a deadlock
                    //We don't get in the completed() method, thus never calling notify()
                    outChannel.write(buffer.asReadOnlyBuffer(), outChannel.size());
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final Object lock;

    public ReadHandler() {
        this.lock = new Object();
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        synchronized (this.lock) {
            //Flip the buffer so it can be written
            if (result > 0) {
                System.out.println(Thread.currentThread().getName() + " finished reading data.");

                attachment.flip();

                //We notify the other threads
                this.lock.notifyAll();
            }
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        throw new RuntimeException("Error occurred while writing a line!");
    }

    public Object getLock() {
        return this.lock;
    }
}