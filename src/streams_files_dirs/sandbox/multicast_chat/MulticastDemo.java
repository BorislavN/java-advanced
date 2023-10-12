package streams_files_dirs.sandbox.multicast_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;

//Each client both sends and receives messages
//Usernames can be duplicate
//Type "/quit" to quit
public class MulticastDemo {
    private static final int MESSAGE_LIMIT = 256;
    private static final int USERNAME_LIMIT = 30;
    private static final String GROUP_IP = "225.4.5.6";
    private static final int PORT = 6969;

    private static void logError(String message, Throwable err) {
        System.err.printf("%s - %s%n", message, err.getMessage());
    }

    private static String decodeMessage(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static boolean validateUsername(String name) {
        return name.length() <= USERNAME_LIMIT;
    }

    private static boolean validateMessage(String message) {
        return message.length() <= (MESSAGE_LIMIT + USERNAME_LIMIT);
    }

    private static ByteBuffer wrapMessage(String username, String message) {
        return ByteBuffer.wrap(String.format("%s: %s", username, message).getBytes(StandardCharsets.UTF_8));
    }

    private static class Client implements Runnable {
        private final ArrayBlockingQueue<String> pendingMessages;
        private final BufferedReader bufferedReader;
        private final DatagramChannel channel;
        private String username;

        public Client() throws IOException {
            this.pendingMessages = new ArrayBlockingQueue<>(11);
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            this.channel = DatagramChannel.open(StandardProtocolFamily.INET);
            this.username = null;
        }

        private void waitForUsername() {
            System.out.println("Please enter an username...");

            try {
                while (this.username == null) {
                    String input = this.bufferedReader.readLine();

                    if (input == null || !validateUsername(input)) {
                        System.out.println("Invalid username!");
                        continue;
                    }

                    if (input.isBlank()) {
                        System.out.println("Username can not be blank!");
                        continue;
                    }

                    this.username = input;
                }
            } catch (IOException e) {
                logError("Exception occurred while reading username", e);
            }
        }

        private MembershipKey configureClient() {
            MembershipKey key = null;

            try {
                //"lo" is localhost
                //The Client can be used on your LAN network
                //I tested it with my laptop
                //But u need to change "lo" to some other interface
                //I ran "InterfaceLister.java" and picked the interface, that has my PC name on it
                //Did the same for the laptop, and that was it, connected successfully...
                NetworkInterface netI = NetworkInterface.getByName("lo");

                this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                        .bind(new InetSocketAddress(PORT))
                        .setOption(StandardSocketOptions.IP_MULTICAST_IF, netI);

                this.channel.configureBlocking(false);

                InetAddress group = InetAddress.getByName(GROUP_IP);
                key = this.channel.join(group, netI);

            } catch (IOException e) {
                logError("Exception occurred while configuring client", e);
            }

            return key;
        }

        private void closeChannel(MembershipKey key) {
            try {
                if (key != null) {
                    key.drop();
                }

                this.channel.disconnect();
                this.channel.close();
            } catch (IOException e) {
                logError("Exception occurred while shutdown", e);
            }
        }

        @Override
        public void run() {
            this.waitForUsername();

            MembershipKey membership = this.configureClient();

            Thread readerThread = new Thread(new ConsoleReader(this.pendingMessages, this.bufferedReader));
            readerThread.start();

            try {
                while (membership != null && membership.isValid() && readerThread.isAlive()) {
                    //Send message
                    String outMessage = this.pendingMessages.poll();

                    if (outMessage != null) {
                        if ("/quit".equals(outMessage)) {
                            break;
                        }

                        this.channel.send(wrapMessage(this.username, outMessage), new InetSocketAddress(GROUP_IP, PORT));
                    }

                    //Read message
                    ByteBuffer buffer = ByteBuffer.allocate(USERNAME_LIMIT + MESSAGE_LIMIT);

                    SocketAddress address = this.channel.receive(buffer);

                    if (address != null) {
                        System.out.println(decodeMessage(buffer.flip()));
                        System.out.println(address);
                    }
                }
            } catch (IOException e) {
                logError("Client encountered an exception", e);
            } finally {
                this.closeChannel(membership);
            }
        }
    }

    private static final class ConsoleReader implements Runnable {
        private final ArrayBlockingQueue<String> pendingMessages;
        private final BufferedReader bufferedReader;

        private ConsoleReader(ArrayBlockingQueue<String> pendingMessages, BufferedReader bufferedReader) {
            this.pendingMessages = pendingMessages;
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {
            String message;

            try {
                do {
                    message = bufferedReader.readLine();

                    if (message == null || !validateMessage(message)) {
                        System.out.println("Invalid message!");
                        continue;
                    }

                    if (message.isBlank()) {
                        System.out.println("Message con not be blank!");
                        continue;
                    }

                    this.pendingMessages.offer(message);

                } while (!"/quit".equals(message));

            } catch (IOException e) {
                logError("ConsoleReader encountered exception", e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
