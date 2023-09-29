package streams_files_dirs.sandbox.chat_app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.channels.SelectionKey.*;
import static streams_files_dirs.sandbox.chat_app.ChatUtility.*;

//This is supposed to be demo chat-app
//TODO: Currently has manny bugs, will need serious debuting :D
public class ChatServer implements Runnable {
    private final ServerSocketChannel server;
    private final Selector mainSelector;
    private final Selector writeSelector;
    private final Set<String> takenUsernames;
    private long numberOfConnections;
    private boolean receivedAConnection;

    public ChatServer() throws IOException {
        this.server = ServerSocketChannel.open();
        this.server.bind(new InetSocketAddress(HOST, PORT));
        this.mainSelector = Selector.open();
        this.writeSelector = Selector.open();
        this.takenUsernames = new HashSet<>();
        this.numberOfConnections = 0;
        this.receivedAConnection = false;

        this.server.configureBlocking(false);
        this.server.register(mainSelector, OP_ACCEPT);
    }

    @Override
    public void run() {
        while (this.hasConnections()) {
            try {
                Iterator<SelectionKey> mainIterator = getReadySet(this.mainSelector);

                while (mainIterator != null && mainIterator.hasNext()) {
                    SelectionKey key = mainIterator.next();

                    this.handleConnection(key);
                    this.handleIncomingData(key);

                    mainIterator.remove();
                }

                Iterator<SelectionKey> writableIterator = getReadySet(this.writeSelector);

                while (writableIterator != null && writableIterator.hasNext()) {
                    SelectionKey key = writableIterator.next();

                    this.handlePendingMessages(key);

                    writableIterator.remove();
                }
            } catch (IOException | IllegalStateException | IllegalArgumentException e) {
                System.err.println("Server encountered an Exception - " + e.getMessage());
            }
        }

        this.shutdown();
    }

    private void handleConnection(SelectionKey key) throws IOException {
        if (key.isValid() && key.isAcceptable()) {
            SocketChannel connection = this.server.accept();

            if (connection != null) {
                connection.configureBlocking(false);
                this.numberOfConnections++;

                connection.register(this.mainSelector, OP_READ, new ConnectionAttachment());

                writeMessage(connection, "Please enter a username:");
            }
        }
    }

    private void handleIncomingData(SelectionKey key) throws IOException, IllegalStateException, IllegalArgumentException {
        //If fore some reason the key is invalid, remove the connection
        if (!key.isValid()) {
            this.removeConnection(key);

            return;
        }

        if (key.isReadable()) {
            String message = readMessage(key);

            if (handleQuit(key, message)) {
                return;
            }

            if (handleSetUsername(key, message)) {
                message = message.substring(6) + " joined the chat!";
            } else {
                //Add the username before the message
                message = ConnectionAttachment.getUsername(key) + ": " + message;
            }

            this.log(message);

            //Add the message to the pending lists, and register the channels in the writeSelector
            for (SelectionKey connection : this.getAllConnections()) {
                if (connection.isValid()) {
                    SocketChannel channel = (SocketChannel) connection.channel();

                    ConnectionAttachment.enqueueMessage(connection, message);

                    channel.register(this.writeSelector, OP_WRITE, connection.attachment());
                }
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

            int bytesWritten = writeMessage(key, message);

            //If write succeeded poll the message
            if (bytesWritten != 0) {
                ConnectionAttachment.pollMessage(key);
            }
        }
    }

    private boolean handleQuit(SelectionKey key, String message) throws IOException, IllegalStateException {
        if (message != null && message.startsWith("/quit")) {
            this.removeConnection(key);

            return true;
        }

        return false;
    }

    private void closeChannel(SelectionKey key) throws IOException {
        key.cancel();
        key.channel().close();
    }

    private void removeConnection(SelectionKey key) throws IOException {
        String name = key.attachment() == null ? "Anonymous" : ConnectionAttachment.getUsername(key);
        log(name + " left the chat...");

        this.takenUsernames.remove(name);
        this.numberOfConnections--;

        this.closeChannel(key);
    }


    private boolean handleSetUsername(SelectionKey key, String message) throws IllegalArgumentException, IllegalStateException, IOException {
        if (message != null && message.startsWith("/user")) {
            message = message.substring(6);

            if (this.takenUsernames.contains(message)) {
                writeMessage(key, message + " is already taken!");
            } else {
                boolean wasSet = ConnectionAttachment.setUsername(key, message);

                if (wasSet) {
                    this.takenUsernames.add(message);

                    return true;
                }
            }
        }

        return false;
    }

    public void shutdown() {
        this.numberOfConnections = 0;
        this.receivedAConnection = true;

        try {
            this.mainSelector.close();
            this.writeSelector.close();
            this.server.close();
        } catch (IOException e) {
            System.err.println("Exception happened while server shutdown - " + e.getMessage());
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
        return this.numberOfConnections > 0 || !this.receivedAConnection;
    }

    private Set<SelectionKey> getAllConnections() {
        return this.mainSelector.keys().stream().filter(k -> k.attachment() != null).collect(Collectors.toSet());
    }

    private void log(String message) {
        System.out.printf("Server log - %s%n", message);
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.run();
    }
}
