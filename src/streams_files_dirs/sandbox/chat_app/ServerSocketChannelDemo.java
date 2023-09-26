package streams_files_dirs.sandbox.chat_app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import static java.nio.channels.SelectionKey.*;
import static java.nio.charset.StandardCharsets.UTF_8;

//Class intended to be used with SocketChanelDemo.java
//TODO:Currently after a user leaves the chat it is closed, in spite of having other users
//TODO:The messages are currently only logged in the server, the pla is to broadcast each message to all connected channels
public class ServerSocketChannelDemo {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8080, 30);
        server.run();
    }

    private static class Server implements Runnable {
        private final ArrayBlockingQueue<String> pendingMessages;
        private final ServerSocketChannel server;
        private final Selector selector;
        private final int charsLimit;
        private boolean receivedAConnection;
        private int cancelledChannels;

        public Server(int port, int charsLimit) throws IOException {
            this.selector = Selector.open();
            this.server = ServerSocketChannel.open();
            this.server.bind(new InetSocketAddress(port));
            this.server.configureBlocking(false);
            this.charsLimit = charsLimit;
            this.receivedAConnection = false;
            this.pendingMessages = new ArrayBlockingQueue<>(21);
            this.cancelledChannels = 0;

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

                        //Client ready for write
                        if (key.isWritable()) {
                            //TODO:add method to send messages from
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
            this.pendingMessages.offer(message);

            System.out.println("Server log - " + message);
        }

        private void quitChannel(SelectionKey key, SocketChannel channel) throws IOException {
            this.addMessage(key.attachment() + " left the chat.");

            if (channel.isOpen()) {
                channel.close();
                this.cancelledChannels++;
            }
        }

        private void handleIncomingMessage(SelectionKey key) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(this.charsLimit);
            SocketChannel connection = (SocketChannel) key.channel();

            String message;

            if (connection.read(buffer) > 0) {
                message = UTF_8.decode(buffer.flip()).toString();

                if (message.startsWith("/quit")) {
                    quitChannel(key, connection);

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
                connection.register(this.selector, OP_READ | OP_WRITE, username);

                this.receivedAConnection = true;
            }
        }

        //Returns true if there are clients connected to the server
        //Returns true if the server hasn't received even a single connection (we don't want to shut down the sever before accepting a connection :D)
        private boolean hasConnections() {
            System.out.println(this.selector.keys().size() - this.cancelledChannels);
            return (this.selector.keys().size() - this.cancelledChannels) > 1 || !this.receivedAConnection;
        }
    }
}