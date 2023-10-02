package streams_files_dirs.sandbox.chat_app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.channels.SelectionKey.*;

//Use "/user {name}" to choose a username
//Use "/quit" to quit
public class ChatServer implements Runnable {
    private final ServerSocketChannel server;
    private final Selector mainSelector;
    private final Selector writeSelector;
    private final Set<String> takenUsernames;
    private boolean receivedAConnection;

    public ChatServer() throws IOException {
        this.server = ServerSocketChannel.open();
        this.server.bind(new InetSocketAddress(ChatUtility.HOST, ChatUtility.PORT));
        this.mainSelector = Selector.open();
        this.writeSelector = Selector.open();
        this.takenUsernames = new HashSet<>();
        this.receivedAConnection = false;

        this.server.configureBlocking(false);
        this.server.register(mainSelector, OP_ACCEPT);
    }

    @Override
    public void run() {
        while (this.hasConnections()) {
            try {
                //Check mainSelector for events
                checkSelectorForEvents(this.mainSelector, "read");
                //Check writeSelector for events
                checkSelectorForEvents(this.writeSelector, "write");

            } catch (IOException e) {
                this.logError("Server encountered an Exception", e);
            }
        }

        this.shutdown();
    }

    private void checkSelectorForEvents(Selector selector, String type) throws IOException {
        Iterator<SelectionKey> iterator = this.getReadySet(selector);

        while (iterator != null && iterator.hasNext()) {
            SelectionKey key = iterator.next();

            try {
                if ("read".equals(type)) {
                    this.handleConnection(key);
                    this.handleIncomingData(key);
                }

                if ("write".equals(type)) {
                    this.handlePendingMessages(key);
                }
            } catch (IllegalArgumentException e) {
                ChatUtility.writeMessage(key, e.getMessage());
            } catch (SocketException | IllegalStateException e) {
                this.removeConnection(key);
            }

            iterator.remove();
        }
    }

    private void handleConnection(SelectionKey key) throws IOException, IllegalStateException {
        if (key.isValid() && key.isAcceptable()) {
            SocketChannel connection = this.server.accept();

            if (connection != null) {
                connection.configureBlocking(false);
                this.receivedAConnection = true;

                connection.register(this.mainSelector, OP_READ, new ConnectionAttachment());

                ChatUtility.writeMessage(connection, "Welcome! Please choose a username.");
            }
        }
    }

    private void handleIncomingData(SelectionKey key) throws IOException, IllegalArgumentException, IllegalStateException {
        if (key.isValid() && key.isReadable()) {
            String message = ChatUtility.readMessage(key);

            if (this.handleQuit(key, message)) {
                return;
            }

            if (this.handleSetUsername(key, message)) {
                message = ChatUtility.joinMessage(message);
            } else {
                message = ConnectionAttachment.getUsername(key) + ": " + message;
            }

            this.log(message);
            this.enqueueMessage(message);
        }
    }

    //Add the message to the pending lists, and register the channels in the writeSelector
    private void enqueueMessage(String message) throws IOException {
        for (SelectionKey connection : this.getAllConnections()) {
            if (connection.isValid()) {
                SocketChannel channel = (SocketChannel) connection.channel();
                channel.register(this.writeSelector, OP_WRITE, connection.attachment());

                ConnectionAttachment.enqueueMessage(connection, message);
            }
        }
    }


    private void handlePendingMessages(SelectionKey key) throws IOException, IllegalStateException {
        if (key.isValid() && key.isWritable()) {
            String message = ConnectionAttachment.peekMessage(key);

            //Unregister if queue is empty
            if (message == null) {
                key.cancel();
                return;
            }

            int bytesWritten = ChatUtility.writeMessage(key, message);

            //If write succeeded poll the message
            if (bytesWritten != 0) {
                ConnectionAttachment.pollMessage(key);
            }
        }
    }

    private boolean handleQuit(SelectionKey key, String message) throws IOException {
        if (message != null && message.startsWith("/quit")) {
            this.removeConnection(key);
            return true;
        }

        return false;
    }

    private void removeConnection(SelectionKey key) throws IOException {
        this.log(ChatUtility.leftMessage(key, this.takenUsernames));

        key.cancel();
        key.channel().close();
    }

    //TODO: Need to make it so when an user changes his username, a message is send
    private boolean handleSetUsername(SelectionKey key, String message) throws IOException, IllegalStateException, IllegalArgumentException {
        if (message != null && message.startsWith("/user")) {
            //Substring only the username
            message = ChatUtility.substringMessage(message, 6);
            String currentName = ConnectionAttachment.getUsername(key);

            if (message.equals(currentName)) {
                ChatUtility.writeMessage(key, message + " already is your username!");
            }

            if (this.takenUsernames.contains(message) || "Anonymous".equalsIgnoreCase(message)) {
                ChatUtility.writeMessage(key, message + " is already taken!");
            }

            if (this.takenUsernames.contains(message) || "Anonymous".equalsIgnoreCase(message)) {
                //Inform client that the name is taken
                ChatUtility.writeMessage(key, message + " is already taken!");
            }

            //Set the username in the attachment
            ConnectionAttachment.setUsername(key, message);
            this.takenUsernames.add(message);

            return true;
        }

        return false;
    }

    public void shutdown() {
        this.receivedAConnection = true;

        try {
            this.mainSelector.close();
            this.writeSelector.close();
            this.server.close();
        } catch (IOException e) {
            this.logError("Exception happened while server shutdown", e);
        }
    }

    private Iterator<SelectionKey> getReadySet(Selector selector) throws IOException {
        int readyCount = selector.selectNow();

        if (readyCount > 0) {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            return selectedKeys.iterator();
        }

        return null;
    }

    private boolean hasConnections() {
        return !this.getAllConnections().isEmpty() || !this.receivedAConnection;
    }

    private Set<SelectionKey> getAllConnections() {
        return this.mainSelector.keys().stream().filter(k -> k.attachment() != null).collect(Collectors.toSet());
    }

    private void log(String message) {
        System.out.printf("[%1$tH:%1$tM] Server log - %2$s%n", LocalTime.now(), message);
    }

    private void logError(String message, Throwable error) {
        System.err.printf("[%1$tH:%1$tM] Server log - \"%2$s - %3$s\"%n", LocalTime.now(), message, error.getMessage());
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.run();
    }
}