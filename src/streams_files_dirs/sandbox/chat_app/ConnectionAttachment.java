package streams_files_dirs.sandbox.chat_app;

import java.nio.channels.SelectionKey;
import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectionAttachment {
    private String username;
    private final Queue<String> pendingMessages;

    public ConnectionAttachment() {
        this.username = null;
        this.pendingMessages = new ArrayDeque<>();
    }

    public boolean enqueueMessage(String message) {
        return this.pendingMessages.offer(message);
    }

    public boolean queueIsEmpty() {
        return this.pendingMessages.isEmpty();
    }

    public String getName() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String pollMessage() {
        return this.pendingMessages.poll();
    }

    public String peekMessage() {
        return this.pendingMessages.peek();
    }

    public static boolean setUsername(SelectionKey key, String name) throws IllegalArgumentException, IllegalStateException {
        if (key.attachment() != null) {
            ((ConnectionAttachment) key.attachment()).setUsername(ChatUtility.validateUsername(name));

            return true;
        }

        throw new IllegalStateException("Attachment is null!");
    }

    public static boolean enqueueMessage(SelectionKey key, String message) throws IllegalStateException {
        if (key.attachment() != null) {
            ConnectionAttachment attachment = (ConnectionAttachment) key.attachment();

            return attachment.enqueueMessage(message);
        }

        throw new IllegalStateException("Attachment is null!");
    }

    public static String pollMessage(SelectionKey key) throws IllegalStateException {
        if (key.attachment() != null) {
            ConnectionAttachment attachment = (ConnectionAttachment) key.attachment();
            return attachment.pollMessage();
        }

        throw new IllegalStateException("Attachment is null!");
    }

    public static String peekMessage(SelectionKey key) throws IllegalStateException {
        if (key.attachment() != null) {
            ConnectionAttachment attachment = (ConnectionAttachment) key.attachment();
            return attachment.peekMessage();
        }

        throw new IllegalStateException("Attachment is null!");
    }

    public static String getUsername(SelectionKey key) throws IllegalStateException {
        if (key.attachment() != null) {
            return ((ConnectionAttachment) key.attachment()).getName();
        }

        throw new IllegalStateException("Trying to get username while attachment is null!");
    }
}