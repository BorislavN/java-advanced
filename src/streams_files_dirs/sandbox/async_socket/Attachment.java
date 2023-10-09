package streams_files_dirs.sandbox.async_socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Attachment {
    public static final int LIMIT = 512;
    private static final AtomicInteger ID = new AtomicInteger(1);
    private final Object lock;
    private final String name;
    private final AsynchronousSocketChannel channel;
    private final ByteBuffer inBuffer;
    private boolean inRead;
    private boolean inWrite;


    public Attachment(AsynchronousSocketChannel channel) {
        this.lock = new Object();
        this.name = "connection" + ID.getAndIncrement();
        this.channel = channel;
        this.inRead = false;
        this.inWrite = false;
        this.inBuffer = ByteBuffer.allocate(LIMIT);
    }

    public String getName() {
        return this.name;
    }

    public AsynchronousSocketChannel getChannel() {
        return this.channel;
    }

    public boolean isInRead() {
        synchronized (this.lock) {
            return this.inRead;
        }
    }

    public void setInRead(boolean value) {
        synchronized (this.lock) {
            this.inRead = value;
        }
    }

    public boolean isInWrite() {
        synchronized (this.lock) {
            return this.inWrite;
        }
    }

    public void setInWrite(boolean value) {
        synchronized (this.lock) {
            this.inWrite = value;
        }
    }

    public ByteBuffer getInBuffer() {
        return this.inBuffer;
    }

    public void closeIfEndOfStream(int result) {
        if (result == -1) {
            try {
                System.out.println("Closing channel, end of stream reached...");

                this.channel.close();
            } catch (IOException e) {
                Attachment.logError("Channel failed to close", e);
            }
        }
    }

    public String decodeMessage() {
        String output = StandardCharsets.UTF_8.decode(this.inBuffer.flip()).toString();
        this.inBuffer.clear();

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Attachment that = (Attachment) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static boolean isValid(String message) {
        return message != null && message.getBytes(StandardCharsets.UTF_8).length <= LIMIT;
    }

    public static void logError(String message, Throwable exc) {
        System.err.printf("%s - %s%n", message, exc.getMessage());
    }

    public static void logMessage(String message) {
        System.out.printf("[%1$tH:%1$tM] Log - \"%2$s\"%n", LocalTime.now(), message);
    }
}