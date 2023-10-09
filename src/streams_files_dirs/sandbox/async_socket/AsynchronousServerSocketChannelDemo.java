package streams_files_dirs.sandbox.async_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AsynchronousServerSocketChannelDemo {
    public static final String HOST = "localhost";
    public static final int PORT = 6969;

    private static class Server implements Runnable {
        private final AsynchronousServerSocketChannel server;
        private Set<Attachment> connections;
        private boolean receivedAConnection;


        public Server() throws IOException {
            this.server = AsynchronousServerSocketChannel.open();
            this.server.bind(new InetSocketAddress(HOST, PORT));
            this.connections = new HashSet<>();
            this.receivedAConnection = false;
        }

        @Override
        public void run() {
            this.server.accept(this, new AcceptHandler());

            while (!this.connections.isEmpty() || !this.receivedAConnection) {
                for (Attachment connection : this.connections) {
                    //Only one read/write operation can be in progress, same as "accept"
                    if (!connection.isInRead()) {
                        connection.setInRead(true);

                        connection.getChannel().read(
                                connection.getInBuffer(), connection, new ReadHandler()
                        );
                    }
                }

                //Clear the closed connections
                this.removeConnections();
            }
        }

        public void addConnection(Attachment attachment) {
            this.connections.add(attachment);
        }

        public void removeConnections() {
            this.connections = this.connections.stream().filter(c -> c.getChannel().isOpen()).collect(Collectors.toSet());
        }

        public AsynchronousServerSocketChannel getServer() {
            return this.server;
        }

        public void setReceivedAConnection(boolean receivedAConnection) {
            this.receivedAConnection = receivedAConnection;
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
        @Override
        public void completed(AsynchronousSocketChannel incomingConnection, Server server) {
            Attachment attachment = new Attachment(incomingConnection);

            server.setReceivedAConnection(true);
            server.addConnection(attachment);

            //Reset the accept method, after the current is finished
            //Only one accept operation can be in progress at a time
            server.getServer().accept(server, this);
        }

        @Override
        public void failed(Throwable exc, Server server) {
            Attachment.logError("Server failed to accept connection", exc);
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attachment) {
            //Print received message
            if (result > 0) {
                Attachment.logMessage(attachment.decodeMessage());
            }

            //Signal read has finished
            attachment.setInRead(false);

            //Close channel if end of stream
            attachment.closeIfEndOfStream(result);
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            Attachment.logError("Read failed", exc);
        }
    }

    //
//    private static class WriteHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
//
//    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}