package streams_files_dirs.sandbox.async_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;

public class AsynchronousServerSocketChannelDemo {
    public static final String HOST = "localhost";
    public static final int PORT = 6969;

    private static class Server implements Runnable {
        private final AsynchronousServerSocketChannel server;


        public Server() throws IOException {
            this.server = AsynchronousServerSocketChannel.open();
            this.server.bind(new InetSocketAddress(HOST, PORT));
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            this.server.accept(this, new AcceptHandler());

            System.out.println("Type \"exit\" to shutdown the server");

            while (true) {
                if ("exit".equals(scanner.nextLine())) {
                    break;
                }
            }
        }

        public AsynchronousServerSocketChannel getServer() {
            return this.server;
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
        @Override
        public void completed(AsynchronousSocketChannel incomingConnection, Server server) {
            Attachment attachment = new Attachment(incomingConnection);

            //Reset the accept method, after the current is finished
            //Only one accept operation can be in progress at a time
            server.getServer().accept(server, this);

            //Call the read method
            incomingConnection.read(attachment.getInBuffer(), attachment, new ReadHandler());
        }

        @Override
        public void failed(Throwable exc, Server server) {
            Attachment.logError("Server failed to accept connection", exc);
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attachment) {
            if (attachment.closeIfEndOfStream(result)) {

                return;
            }

            if (result > 0) {
                //"decode" clears the buffer before returning the message
                Attachment.logMessage(attachment.decodeMessage());
            }

            //Reset the read method
            //Only one read/write operation can be in progress per channel
            attachment.getChannel().read(attachment.getInBuffer(), attachment, this);
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            Attachment.closeChannel(attachment.getChannel());
            Attachment.logError("Read failed", exc);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}