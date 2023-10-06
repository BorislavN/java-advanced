package streams_files_dirs.sandbox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//TODO: figure out why no messages are read
public class AsynchronousServerSocketChannelDemo {
    public static final int PORT = 6969;
    public static final int LIMIT = 512;

    private static class Server implements Runnable {
        private String message;
        private final AsynchronousServerSocketChannel server;
        private final List<AsynchronousSocketChannel> connections;


        public Server() throws IOException {
            this.server = AsynchronousServerSocketChannel.open();
            this.server.bind(new InetSocketAddress("localhost",PORT));
            this.connections = new ArrayList<>();
            message = "";
        }

        public void addConnection(AsynchronousSocketChannel channel) {
            this.connections.add(channel);
        }

        public AsynchronousServerSocketChannel getServer() {
            return this.server;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            this.server.accept(this, new AcceptHandler());

            while (!message.equals("quit")) {
                for (AsynchronousSocketChannel connection : connections) {
                    ByteBuffer buffer = ByteBuffer.allocate(LIMIT);

                    connection.read(buffer,buffer,new ReadHandler());
                }
            }
        }
    }

    private static class ReadHandler implements CompletionHandler<Integer,ByteBuffer> {

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            if (result>0){
                System.out.println("Message received: "+StandardCharsets.UTF_8.decode(buffer));
            }else {
                System.out.println("No data");
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer buffer) {
            System.err.println(exc.getMessage());
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {

        @Override
        public void completed(AsynchronousSocketChannel client, Server server) {
            server.addConnection(client);
            System.out.println("Connection accepted!");

            server.getServer().accept(server, this);
        }

        @Override
        public void failed(Throwable exc, Server server) {
            System.err.println(exc.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
