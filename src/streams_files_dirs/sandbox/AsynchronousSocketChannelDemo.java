package streams_files_dirs.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AsynchronousSocketChannelDemo {

    private static class Client implements Runnable {
        private final AsynchronousSocketChannel client;
        private final BufferedReader reader;

        public Client() throws IOException {
            this.client = AsynchronousSocketChannel.open();
            this.client.connect(new InetSocketAddress("localhost",AsynchronousServerSocketChannelDemo.PORT), null, new ConnectionHandler());
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {
            try {
                String message;

                do {
                    message = reader.readLine();

                    if (message == null) {
                        System.out.println("Message is null!");
                        continue;
                    }

                    if (message.getBytes(StandardCharsets.UTF_8).length > AsynchronousServerSocketChannelDemo.LIMIT) {
                        System.out.println("Message too long!");
                        continue;
                    }

                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

                    this.client.write(buffer, buffer, new WriteHandler());


                } while (!"quit".equals(message));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ConnectionHandler implements CompletionHandler<Void, Object> {
        @Override
        public void completed(Void result, Object attachment) {
            System.out.println("Connected successfully!");
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.err.println(exc.getMessage());
        }
    }

    private static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (result > 0) {
                attachment.flip();
                attachment.limit(result);

                System.out.println("Message sent successfully! - " + StandardCharsets.UTF_8.decode(attachment));
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            System.err.println(exc.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
