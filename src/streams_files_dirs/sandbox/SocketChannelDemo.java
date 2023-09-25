package streams_files_dirs.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

//Class intended to be used with ServerSocketChanelDemo.java
public class SocketChannelDemo {
    public static void main(String[] args) {

        try (
                SocketChannel connection = SocketChannel.open(new InetSocketAddress("localhost", 8080));
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            String message = "";

            while (!"STOP".equals(message = reader.readLine())) {
                ByteBuffer buffer = ByteBuffer.allocate(256);

                //send message
                connection.write(ByteBuffer.wrap(message.getBytes()));

                //read response
                while (connection.read(buffer) > 0) {
                    System.out.println(UTF_8.decode(buffer.flip()));
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
