package streams_files_dirs.sandbox.multicast_chat;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.StandardCharsets;

//TODO: make something actually useful, currently this "thing" just sends one word :D
//I wanted to see if "it" will even start

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
       client.run();
    }

    private static class Client implements Runnable {
        private DatagramChannel channel;

        public Client() throws IOException {
            this.channel = DatagramChannel.open(StandardProtocolFamily.INET)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress(6969));
        }

        @Override
        public void run() {
            try {
                NetworkInterface ni = NetworkInterface.getByName("lo");

                this.channel.setOption(StandardSocketOptions.IP_MULTICAST_IF,ni);

                InetAddress group = InetAddress.getByName("225.4.5.6");

                MembershipKey key = this.channel.join(group, ni);

                System.out.println(this.channel.getLocalAddress());
                System.out.println(this.channel.getRemoteAddress());
                System.out.println();

                if (key.isValid()){
                   this.channel.send(ByteBuffer.wrap("heeeeeeey".getBytes(StandardCharsets.UTF_8)),new InetSocketAddress(group,6969));
                }

                while (key.isValid()){
                    ByteBuffer buffer=ByteBuffer.allocate(256);
                    this.channel.receive(buffer);

                    System.out.println(StandardCharsets.UTF_8.decode(buffer.flip()));
                }

            } catch (IOException e) {
                System.err.println("Exception happened - "+e.getMessage());
            }
        }
    }
}
