package streams_files_dirs.sandbox.chat_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static streams_files_dirs.sandbox.chat_app.ChatUtility.HOST;
import static streams_files_dirs.sandbox.chat_app.ChatUtility.PORT;

public class ChatClient implements Runnable {
    private final SocketChannel client;
    private final Queue<String> messageQueue;
    private final BufferedReader bufferedReader;
    private String tempUsername;
    private boolean hasUsername;

    public ChatClient() throws IOException {
        this.client = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        this.messageQueue = new ArrayBlockingQueue<>(21);
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.tempUsername = "";
        this.hasUsername = false;

        this.client.configureBlocking(false);
    }

    @Override
    public void run() {
        ConsoleReader reader = new ConsoleReader(this.bufferedReader, this.messageQueue);
        Thread readerThread = new Thread(reader);
        readerThread.start();

        String message;

        do {
            message = this.messageQueue.poll();

            try {
                if (message != null) {
                    if (!hasUsername) {
                        this.tempUsername = message;
                        message = "/user " + message;
                    }

                    ChatUtility.writeMessage(this.client, message);
                }

                String response = ChatUtility.readMessage(this.client);

                //Check if username was set
                if (this.tempUsernameWelcome().equals(response)) {
                    this.hasUsername = true;
                }

               if (!response.isBlank()){
                   System.out.println(response);
               }

            } catch (IOException | IllegalStateException e) {
                System.err.println("Client encountered exception - " + e.getMessage());
            }
        } while (!"/quit".equals(message));

        this.shutdown();
    }

    private void shutdown() {
        try {
            this.bufferedReader.close();
            this.client.close();
        } catch (IOException e) {
            System.err.println("Encountered exception while trying to close client - " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        client.run();
    }

    private String tempUsernameWelcome() {
        return this.tempUsername + " joined the chat!";
    }
}
