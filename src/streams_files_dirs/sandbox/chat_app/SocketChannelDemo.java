package streams_files_dirs.sandbox.chat_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import static java.nio.charset.StandardCharsets.UTF_8;

//Class intended to be used with ServerSocketChanelDemo.java
public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        Client client = new Client(8080, 10, 30);
        client.run();
    }

    private static class Client implements Runnable {
        private final ArrayBlockingQueue<String> messageQueue;
        private final SocketChannel client;
        private final int charLimit;
        private final int usernameLimit;

        public Client(int port, int usernameLimit, int charLimit) throws IOException {
            this.charLimit = charLimit;
            this.usernameLimit = usernameLimit;
            this.client = SocketChannel.open(new InetSocketAddress(port));
            this.messageQueue = new ArrayBlockingQueue<>(10);
        }

        @Override
        public void run() {
            InputReader inputReader = new InputReader(this);
            Thread inputThread = new Thread(inputReader);
            inputThread.start();

            String message;

            ByteBuffer inBuffer = ByteBuffer.allocate(this.charLimit + this.usernameLimit);
            ByteBuffer outBuffer;

            try {
                this.client.configureBlocking(false);

                do {
                    message = this.messageQueue.poll();

                    //Check for incoming messages
                    int bytesRead = this.client.read(inBuffer);

                    if (bytesRead > 0) {
                        System.out.println(UTF_8.decode(inBuffer.flip()));
                        inBuffer.clear();
                    }

                    //Check for outgoing messages
                    if (message != null) {
                        outBuffer = ByteBuffer.wrap(message.getBytes());

                        while (outBuffer.hasRemaining()) {
                            int bytesWritten = this.client.write(outBuffer);
                            outBuffer.position(bytesWritten);
                        }
                    }
                } while (!"/quit".equals(message));

            } catch (IOException | IllegalStateException e) {
                System.err.printf("Client in Thread: \"%s\" encountered and exception!%n", Thread.currentThread().getName());
                e.printStackTrace();
            } finally {
                this.shutdownClient();
            }
        }

        private void shutdownClient() {
            if (this.client.isOpen()) {
                try {
                    this.client.shutdownInput();
                    this.client.shutdownOutput();
                    this.client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void addMessage(String message) {
            this.messageQueue.offer(message);
        }

        public int getCharLimit() {
            return this.charLimit;
        }

        public int getUsernameLimit() {
            return this.usernameLimit;
        }
    }

    private static class InputReader implements Runnable {
        private final Client client;
        private final BufferedReader bufferedReader;
        private boolean hasSetUsername;


        private InputReader(Client client) {
            this.client = client;
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            this.hasSetUsername = false;
        }

        @Override
        public void run() {
            String input;

            try {
                do {
                    input = bufferedReader.readLine();

                    if (input.isBlank()) {
                        System.out.println("Input can't be blank!");
                        continue;
                    }

                    if (!this.hasSetUsername) {
                        if (input.getBytes().length > this.client.getUsernameLimit()) {
                            System.out.println("Username too long!");
                            continue;
                        }

                        this.hasSetUsername = true;
                    }

                    if (input.getBytes().length > this.client.getCharLimit()) {
                        System.out.println("Message too long!");
                        continue;
                    }

                    this.client.addMessage(input);

                } while (!"/quit".equals(input));

                this.bufferedReader.close();
            } catch (IOException e) {
                System.err.printf("\"%s\" encountered and IOException while reading input!%n", Thread.currentThread().getName());
            }
        }
    }
}