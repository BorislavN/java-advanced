package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.StandardOpenOption.*;

public class AsynchronousDemoWithReentrantLock {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync2.txt");

        try (
                AsynchronousFileChannel inChannel = AsynchronousFileChannel.open(filePath, READ);
                AsynchronousFileChannel outChannel = AsynchronousFileChannel.open(copyPath, TRUNCATE_EXISTING, WRITE, CREATE)
        ) {
            //the closing of the channels releases the locks
            acquireFileLock(inChannel, true);
            acquireFileLock(outChannel, false);

            ByteBuffer buffer = ByteBuffer.allocate(256);
            ReadHandlerWithLock handler = new ReadHandlerWithLock();

            while ((inChannel.size() > outChannel.size())) {
                inChannel.read(buffer, outChannel.size(), buffer, handler);
                handler.waitForData();//Waiting for data

                //Exit the loop without writing to the file
                if (handler.readFailed()) {
                    break;
                }

                outChannel.write(buffer.asReadOnlyBuffer(), outChannel.size());
            }


        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private static void acquireFileLock(AsynchronousFileChannel channel, boolean isShared) throws IOException {
        FileLock lock = null;

        //Loops until it locks, the lock is later released after closing the underlying chanel
        while (lock == null) {
            lock = channel.tryLock(0, Long.MAX_VALUE, isShared);
        }
    }
}

class ReadHandlerWithLock implements CompletionHandler<Integer, ByteBuffer> {
    private final ReentrantLock lock;
    private final Condition dataReady;
    private boolean readFinished;
    private boolean readFailed;

    public ReadHandlerWithLock() {
        this.lock = new ReentrantLock(true);
        this.dataReady = lock.newCondition();
        this.readFinished = false;
        this.readFailed = false;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            this.lock.lock();

            if (result > 0) {
                attachment.flip();
                this.readFinished = true;//set the flag to true
                this.dataReady.signalAll();//signal the other threads

                System.out.println(Thread.currentThread().getName() + " finished reading data.");
                return;
            }

            this.failed(new Throwable("No data was read!"), attachment);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.lock.lock();

            this.readFailed = true;
            this.dataReady.signalAll();

            System.err.printf("Thread: %s - Exception: %s%n"
                    , Thread.currentThread().getName()
                    , exc.getMessage());
        } finally {
            this.lock.unlock();
        }
    }

    public void waitForData() {
        try {
            this.lock.lock();

            while (!this.readFinished) {
                if (this.readFailed) {
                    return;//Wakeup and continue
                }

                this.dataReady.awaitUninterruptibly();
            }

            //reset the flag
            //so the reading of the file can continue correctly
            this.readFinished = false;

        } finally {
            this.lock.unlock();
        }
    }

    public boolean readFailed() {
        try {
            lock.lock();

            return this.readFailed;
        } finally {
            lock.unlock();
        }
    }
}