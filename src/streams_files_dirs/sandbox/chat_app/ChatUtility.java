package streams_files_dirs.sandbox.chat_app;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChatUtility {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int MESSAGE_LIMIT = 300;
    public static final int USERNAME_LIMIT = 30;

    public static String readMessage(SelectionKey key) throws IOException, IllegalStateException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            return read(channel);
        } else {
            throw new IllegalStateException("Key invalid!");
        }
    }

    public static int writeMessage(SelectionKey key, String message) throws IOException, IllegalStateException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            return write(channel, message);
        } else {
            throw new IllegalStateException("Key invalid!");
        }
    }

    public static int writeMessage(SocketChannel channel, String message) throws IOException, IllegalStateException {
        return write(channel, message);
    }

    public static String readMessage(SocketChannel channel) throws IOException {
        return read(channel);
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

        //Check if connection was closed
        if (bytesRead == -1) {
            channel.close();
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

        //Check if connection was closed
        if (totalBytes == -1) {
            channel.close();
        }

        return totalBytes;
    }

    private static String decodeBuffer(ByteBuffer buffer) {
        return UTF_8.decode(buffer).toString();
    }

    private static void checkIfSocketIsConnected(SocketChannel channel) throws IllegalStateException {
        if (!(channel.isOpen() && channel.isConnected())) {
            throw new IllegalStateException("Socket is not open/connected!");
        }
    }

    public static String validateUsername(String name) throws IllegalArgumentException {
        if (name.getBytes().length > USERNAME_LIMIT) {
            throw new IllegalArgumentException("Username too long!");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Username can not be blank!");
        }

        return name;
    }

    public static String joinMessage(String message) {
        return substringMessage(message, 6) + " joined the chat!";
    }

    public static String leftMessage(SelectionKey key, Set<String> takenNames) {
        String name = key.attachment() == null ? "Anonymous" : ConnectionAttachment.getUsername(key);
        takenNames.remove(name);

        return name + " left the chat...";
    }

    private static String substringMessage(String message, int start) {
        return message.substring(start);
    }
}