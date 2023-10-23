package multicast_chat_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.StandardCharsets;

//Here im just doing experiments with the socket configuration :D
//Dark magic
//Type "1", "2", "3" for different configurations
//Type "get", "post" for channel type

//Start the receiver channel first, then tha sender channel
//If you do it the other way around, some packets are missed
public class DatagramPlayground {
    private static final String GROUP_IP = "225.4.5.6";
    private static final int PORT = 6969;
    private static final InetSocketAddress HOME_ADDRESS = new InetSocketAddress("127.0.0.1", PORT);
    private static final InetSocketAddress GROUP_ADDRESS = new InetSocketAddress(GROUP_IP, PORT);
    private static final InetSocketAddress LAN_ADDRESS = new InetSocketAddress("192.168.0.101", PORT);

    private static String decodeMessage(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static ByteBuffer wrapMessage(String message) {
        return ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
    }

    private static class Client implements Runnable {
        private final BufferedReader bufferedReader;
        private final DatagramChannel channel;
        private String config;

        public Client() throws IOException {
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            this.channel = DatagramChannel.open(StandardProtocolFamily.INET);
        }

        private MembershipKey configureClient() {
            MembershipKey key = null;

            try {
                System.out.println("Enter configuration number:");
                this.config = this.bufferedReader.readLine();

                NetworkInterface netI = null;

                switch (this.config) {
                    //No host config
                    case "1" -> {
                        netI = NetworkInterface.getByName("lo");

                        this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                .bind(new InetSocketAddress(PORT))
                                .setOption(StandardSocketOptions.IP_MULTICAST_IF, netI);
                    }

                    //Specific host config
                    case "2" -> {
                        netI = NetworkInterface.getByName("eth2");
                        this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                .bind(LAN_ADDRESS)
                                .setOption(StandardSocketOptions.IP_MULTICAST_IF, netI);
                    }

                    //Both config "2" and "3" use the same interface, so you can have sender of one type and receiver of the other
                    //Depending on which channel type you start first, some packets may be missed
                    //Changing the bind address/connect address, in some cases fixes the problem, in other it doesn't
                    case "3" -> {
                        netI = NetworkInterface.getByName("eth2");
                        this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                .bind(LAN_ADDRESS)
//                                .bind(new InetSocketAddress(PORT))
                                .setOption(StandardSocketOptions.IP_MULTICAST_IF, netI);

                        //Config with connect
                        this.channel.connect(LAN_ADDRESS);
//                        this.channel.connect(GROUP_ADDRESS);
                    }

                    default -> {
                        System.err.println("Invalid config");
                    }
                }

                this.channel.configureBlocking(false);

                key = this.channel.join(GROUP_ADDRESS.getAddress(), netI);

            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            MembershipKey membership = this.configureClient();

            try {
                System.out.println("Channel local:" + this.channel.getLocalAddress());
                System.out.println("Channel remote:" + this.channel.getRemoteAddress());
                System.out.println();

                System.out.println("Enter channel type:");
                String type = this.bufferedReader.readLine();

                if (!"post".equals(type) && !"get".equals(type)) {
                    System.err.println("Invalid type!");
                    membership.drop();
                }

                while (membership != null && membership.isValid()) {
                    if ("post".equals(type)) {
                        if ("1".equals(this.config)) {
                            this.channel.send(wrapMessage("Config 1 - Data2 Data2 Data2 target GROUP"), GROUP_ADDRESS);
                            this.channel.send(wrapMessage("Config 1 - Imposter target HOME"), HOME_ADDRESS);
                            this.channel.send(wrapMessage("Config 1 - Imposter target INVALID IP, VALID PORT"), new InetSocketAddress("127.0.5.5", PORT));
                        }

                        if ("2".equals(this.config)) {
                            this.channel.send(wrapMessage("Config 2 - Imposter target LAN"), LAN_ADDRESS);
                            this.channel.send(wrapMessage("Config 2 - Data2 Data2 Data2 target GROUP"), GROUP_ADDRESS);
                        }

                        if ("3".equals(this.config)) {
                            this.channel.write(wrapMessage("Config 3 - Data2 Data2 Data2 target GROUP"));
                            this.channel.write(wrapMessage("Config 3 - Data3 Data4 Data5 target GROUP"));
                        }

                        Thread.sleep(3000);
                    }

                    if ("get".equals(type)) {
                        ByteBuffer buffer = ByteBuffer.allocate(100);

                        if ("1".equals(this.config) || "2".equals(this.config)) {
                            SocketAddress address = this.channel.receive(buffer);

                            if (address != null) {
                                System.out.println(address);
                                System.out.println(decodeMessage(buffer.flip()));
                                System.out.println();
                            }
                        }

                        if ("3".equals(this.config)) {
                            int count = this.channel.read(buffer);

                            if (count > 0) {
                                System.out.println("Bytes " + count);
                                System.out.println(decodeMessage(buffer.flip()));
                                System.out.println();
                            }
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.closeChannel(membership);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}