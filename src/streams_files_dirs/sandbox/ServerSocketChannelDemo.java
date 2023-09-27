package streams_files_dirs.sandbox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.StandardCharsets.UTF_8;

//Simple "Chat" :D server
//Class intended to be used with SocketChanelDemo.java
//Currently the messages are only printed locally in the server as a log
//If the server encounters error, it shuts down
public class ServerSocketChannelDemo {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8080, 30);
        server.run();
    }

    private static class Server implements Runnable {
        private final ServerSocketChannel server;
        private final Selector selector;
        private final int charsLimit;
        private boolean receivedAConnection;
        private int activeChannels;

        public Server(int port, int charsLimit) throws IOException {
            this.selector = Selector.open();
            this.server = ServerSocketChannel.open();
            this.server.bind(new InetSocketAddress(port));
            this.server.configureBlocking(false);
            this.charsLimit = charsLimit;
            this.receivedAConnection = false;
            this.activeChannels = 0;

            this.server.register(this.selector, OP_ACCEPT);
        }

        @Override
        public void run() {
            try {
                while (this.hasConnections()) {
                    //Blocks until at least one event is detected
                    this.selector.select();

                    Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        //Connection accepted event
                        if (key.isAcceptable()) {
                            this.handleIncomingConnection();
                        }

                        //Client ready for read
                        if (key.isReadable()) {
                            this.handleIncomingMessage(key);
                        }

                        iterator.remove();
                    }
                }
            } catch (IOException | IllegalStateException e) {
                System.err.println("Exception happened in Server!");
                e.printStackTrace();
            }
        }

        private void addMessage(String message) {
            System.out.println("Server log - " + message);
        }

        private void quitChannel(SelectionKey key, SocketChannel channel) throws IOException {
            this.addMessage(key.attachment() + " left the chat.");

            if (channel.isOpen()) {
                channel.close();
                this.activeChannels--;
            }
        }

        private void handleIncomingMessage(SelectionKey key) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(this.charsLimit);
            SocketChannel connection = (SocketChannel) key.channel();

            String message;

            if (connection.read(buffer) > 0) {
                message = UTF_8.decode(buffer.flip()).toString();

                if (message.startsWith("/quit")) {
                    this.quitChannel(key, connection);
                    return;
                }

                this.addMessage(key.attachment() + ": " + message);
            }
        }

        private void handleIncomingConnection() throws IOException {
            SocketChannel connection = this.server.accept();

            if (connection != null) {
                connection.configureBlocking(false);

                //Send welcome
                int welcomeBytes;

                do {
                    welcomeBytes = connection.write(ByteBuffer.wrap("Please enter a username:".getBytes()));
                } while (welcomeBytes == 0);

                //wait for username
                int usernameBytes;
                ByteBuffer usernameBuffer = ByteBuffer.allocate(this.charsLimit);

                do {
                    usernameBytes = connection.read(usernameBuffer);
                } while (usernameBytes == 0);

                String username = UTF_8.decode(usernameBuffer.flip()).toString();

                //Add message to queue
                this.addMessage(username + " joined the chat.");

                //Register connection in selector
                connection.register(this.selector, OP_READ, username);
                this.receivedAConnection = true;
                this.activeChannels++;
            }
        }

        //Returns true if there are clients connected to the server
        //Returns true if the server hasn't received even a single connection (we don't want to shut down the sever before accepting a connection :D)
        private boolean hasConnections() {
            return this.activeChannels > 0 || !this.receivedAConnection;
        }
    }
}