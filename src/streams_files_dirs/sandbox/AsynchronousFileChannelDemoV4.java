package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.StandardOpenOption.*;

//Attempting to get the same result as the V3 Demo
//but using a ReentrantLock
//Currently not working, the successful output depends on the writeChanel waiting for the input of the readChannel
//IDK if the problem can be solved without some type of waiting and notifying
public class AsynchronousFileChannelDemoV4 {
    public static void main(String[] args) {
        Path filePath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/asyncData.txt");
        Path copyPath = Path.of("src/streams_files_dirs/exercises/resources/sandbox/copyDataAsync2.txt");

        FileLock inputLock;
        FileLock outputLock;

        ReentrantLock lock = new ReentrantLock(true);//Fairness enabled lock

        try (
                AsynchronousFileChannel inChannel = AsynchronousFileChannel.open(filePath, READ);
                AsynchronousFileChannel outChannel = AsynchronousFileChannel.open(copyPath, TRUNCATE_EXISTING, WRITE, CREATE)
        ) {
            inputLock = aquireFileLock(inChannel, true);
            outputLock = aquireFileLock(outChannel, false);

            ByteBuffer buffer = ByteBuffer.allocate(256);

            ReadHandlerWithLock handler = new ReadHandlerWithLock(lock);

            while (inChannel.size() > outChannel.size()) {
                lock.lock();
                inChannel.read(buffer, outChannel.size(), buffer, handler);
                lock.unlock();

                //TODO: output is all over the place, will try to fix it tomorrow...

                outChannel.write(buffer.asReadOnlyBuffer(), outChannel.size());
            }

            if (inputLock != null && outputLock != null) {
                inputLock.release();
                outputLock.release();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private static FileLock aquireFileLock(AsynchronousFileChannel channel, boolean isShared) throws IOException {
        FileLock lock = null;

        while (lock == null) {
            lock = channel.tryLock(0, Long.MAX_VALUE, isShared);
        }

        return lock;
    }
}

class ReadHandlerWithLock implements CompletionHandler<Integer, ByteBuffer> {
    private final ReentrantLock lock;

    public ReadHandlerWithLock(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            this.lock.lock();

            if (result > 0) {
                System.out.println(Thread.currentThread().getName() + " finished reading data.");
                attachment.flip();
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        throw new RuntimeException("Error occurred while writing a line!");
    }
}