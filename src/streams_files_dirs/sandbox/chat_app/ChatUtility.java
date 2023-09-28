package streams_files_dirs.sandbox.chat_app;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChatUtility {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int MESSAGE_LIMIT = 300;
    public static final int USERNAME_LIMIT = 30;
    public static final int BUFFER_LIMIT = MESSAGE_LIMIT + USERNAME_LIMIT;

    public static String readMessage(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();

            return read(channel);

        } else {
            throw new IllegalStateException("Key invalid!");
        }
    }

    public static int writeMessage(SelectionKey key, String message) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();

            return write(channel, message);

        } else {
            throw new IllegalStateException("Key invalid!");
        }
    }

    public static String read(SocketChannel channel) throws IOException {
        checkIfSocketIsConnected(channel);

        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LIMIT);
        StringBuilder output = new StringBuilder();

        int bytesRead = channel.read(buffer);

        while (bytesRead > 0) {
            output.append(decodeBuffer(buffer.flip()));
            bytesRead = channel.read(buffer.clear());
        }

        return output.toString();
    }

    public static int write(SocketChannel channel, String message) throws IOException {
        checkIfSocketIsConnected(channel);

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(UTF_8));

        int bytesWritten = channel.write(buffer);
        int totalBytes = bytesWritten;

        while (bytesWritten > 0 && buffer.hasRemaining()) {
            bytesWritten = channel.write(buffer);
            totalBytes += bytesWritten;
        }

        return totalBytes;
    }

    private static String decodeBuffer(ByteBuffer buffer) {
        return UTF_8.decode(buffer).toString();
    }

    private static void checkIfSocketIsConnected(SocketChannel channel) {
        if ((channel.isOpen() && channel.isConnected())) {
            throw new IllegalStateException("Socket is not open/connected!");
        }
    }

    private static void validateMessage(String message, String type) {
        if ("incoming".equals(type)) {
            if (message.length() > MESSAGE_LIMIT) {
                throw new IllegalArgumentException("Message too long, bytes-limit: " + MESSAGE_LIMIT);
            }
        }

        if ("outgoing".equals(type)) {
            if (message.length() > BUFFER_LIMIT) {
                throw new IllegalArgumentException("Message too long, bytes-limit: " + BUFFER_LIMIT);
            }
        }
    }
}