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
//Then I'm sending the data to be written
//Without the synchronization I was getting incorrect text in the output file
public class AsynchronousDemoWithSynchronization {
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
                    //In a loop in case the thread wakes up without being signaled
                    while (!handler.hasPendingData()) {
                        if (handler.readHasFailed()) {
                            return;//Exit the loop in case of a read failure
                        }

                        handler.getLock().wait();
                    }

                    //Reset the flag, so the read can continue correctly
                    handler.resetFlag();

                    //We copy the buffer, to avoid sharing it with the other channel
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
    private boolean pendingData;
    private boolean readFailed;

    public ReadHandler() {
        this.lock = new Object();
        this.pendingData = false;
        this.readFailed = false;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        synchronized (this.lock) {
            if (result > 0) {
                //Flip the buffer so it can be written
                attachment.flip();
                this.pendingData = true;

                //We notify the other threads
                this.lock.notifyAll();

                System.out.println(Thread.currentThread().getName() + " finished reading data.");
                return;
            }

            this.failed(new Throwable("No data was read!"), attachment);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        synchronized (this.lock) {
            this.readFailed = true;
            this.lock.notifyAll();

            System.err.println(Thread.currentThread().getName() + " exception occurred - " + exc.getMessage());
        }
    }

    public boolean hasPendingData() {
        synchronized (this.lock) {
            return this.pendingData;
        }
    }

    public void resetFlag() {
        synchronized (this.lock) {
            this.pendingData = false;
        }
    }

    public boolean readHasFailed() {
        synchronized (this.lock) {
            return this.readFailed;
        }
    }

    public Object getLock() {
        return this.lock;
    }
}