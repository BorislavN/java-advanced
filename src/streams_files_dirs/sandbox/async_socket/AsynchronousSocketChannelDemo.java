package streams_files_dirs.sandbox.async_socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;

import static streams_files_dirs.sandbox.async_socket.AsynchronousServerSocketChannelDemo.HOST;
import static streams_files_dirs.sandbox.async_socket.AsynchronousServerSocketChannelDemo.PORT;

public class AsynchronousSocketChannelDemo {
    private static class Client implements Runnable {
        private final Queue<String> pendingMessages;
        private final AsynchronousSocketChannel client;
        private final Attachment attachment;
        private final BufferedReader reader;

        public Client() throws IOException {
            this.pendingMessages = new ArrayDeque<>();
            this.client = AsynchronousSocketChannel.open();
            this.reader = new BufferedReader(new InputStreamReader(System.in));
            this.attachment = new Attachment(this.client);
        }

        @Override
        public void run() {
            this.client.connect(new InetSocketAddress(HOST, PORT), this.attachment, new ConnectionHandler());

            String input;

            try {
                do {
                    input = this.reader.readLine();

                    if (input != null && !input.isBlank()) {
                        this.pendingMessages.offer(input);
                    }

                    if (!this.pendingMessages.isEmpty() && !this.attachment.isInWrite()) {
                        String current = this.pendingMessages.poll();

                        if (Attachment.isValid(current)) {
                            this.attachment.setInWrite(true);

                            ByteBuffer buffer = ByteBuffer.wrap(current.getBytes(StandardCharsets.UTF_8));

                            this.attachment.getChannel().write(buffer, this.attachment, new WriteHandler());

                        } else {
                            System.out.printf("Message - \"%s\" is too long...%n", current);
                        }
                    }

                } while (this.client.isOpen() && !"quit".equals(input));

            } catch (IOException e) {
                Attachment.logError("BufferedReader failed to read", e);
            } finally {
                this.shutdownClient();
            }
        }

        private void shutdownClient() {
            //Close BufferedReader
            try {
                this.reader.close();
            } catch (IOException e) {
                Attachment.logError("BufferedReader failed to close", e);
            }

            //Check for pending messages
            if (!this.pendingMessages.isEmpty()) {
                System.err.println("Client closing, but pendingMessages is not empty!");
            }

            //Close channel
            Attachment.closeChannel(this.client);
        }

        public static void connectedMessage(AsynchronousSocketChannel client) {
            try {
                System.out.printf("Connected successfully to \"%s\"!%n", client.getRemoteAddress());
            } catch (IOException e) {
                Attachment.logError("Failed to get remote address", e);
            }
        }
    }

    private static class ConnectionHandler implements CompletionHandler<Void, Attachment> {
        @Override
        public void completed(Void result, Attachment attachment) {
            Client.connectedMessage(attachment.getChannel());
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            Attachment.logError("Failed to connect to server", exc);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }

    private static class WriteHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attachment) {
            if (result > 0) {
                System.out.printf("Message written successfully - %d bytes sent%n", result);
            }

            attachment.closeIfEndOfStream(result);
            attachment.setInWrite(false);
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            Attachment.closeChannel(attachment.getChannel());
            Attachment.logError("Write failed", exc);

            attachment.setInWrite(false);
        }
    }
}