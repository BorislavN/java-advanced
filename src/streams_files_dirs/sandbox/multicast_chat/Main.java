package streams_files_dirs.sandbox.multicast_chat;

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
public class Main {
    private static final String GROUP_IP = "225.4.5.6";
    private static final int PORT = 6969;

    private static String decodeMessage(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static String getFullAddress() {
        return String.format("%s:%d", GROUP_IP, PORT);
    }

    private static ByteBuffer wrapMessage(String message) {
        return ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
    }

    private static class Client implements Runnable {
        private final BufferedReader bufferedReader;
        private final DatagramChannel channel;
        private final InetSocketAddress fullAddress;

        public Client() throws IOException {
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            this.channel = DatagramChannel.open(StandardProtocolFamily.INET);
            this.fullAddress = new InetSocketAddress( "192.168.0.101",PORT);
        }

        private MembershipKey configureClient() {
            MembershipKey key = null;

            try {
                NetworkInterface netI = NetworkInterface.getByName("eth2");


                this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                        .bind(this.fullAddress)
                        .setOption(StandardSocketOptions.IP_MULTICAST_IF, netI);

                this.channel.configureBlocking(false);
                this.channel.connect(new InetSocketAddress("192.168.0.101",PORT));

//                key = this.channel.join(InetAddress.getByName(GROUP_IP), netI);

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

            System.out.println(this.fullAddress);
            try {
                System.out.println("Enter type...");
                String type = this.bufferedReader.readLine();

//                while (membership != null && membership.isValid()) {
                while (true) {
                    if ("send".equals(type)) {
//                        this.channel.send(wrapMessage("Data2 Data2 Data2..."),new InetSocketAddress(GROUP_IP,PORT));
                        this.channel.write(wrapMessage("Data2 Data2 Data2..."));
                    }

                    if ("get".equals(type)) {
                        ByteBuffer buffer = ByteBuffer.allocate(100);

                        int address = this.channel.read(buffer);

                        if (address>0) {
                            System.out.println(address);
                            System.out.println(decodeMessage(buffer.flip()));
                        }
                    }

                    Thread.sleep(500);
                }
            } catch (InterruptedException | IOException e) {
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