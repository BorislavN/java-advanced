package streams_files_dirs.sandbox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.charset.StandardCharsets.UTF_8;

//Class intended to be used with SocketChanelDemo.java
//TODO: currently after receiving the first message and returning a response, the server/client becomes unresponsive
public class ServerSocketChannelDemo {
    private static final String STOP_SWITCH = "NEMA PARA";

    public static void main(String[] args) {
        String state = "";

        try (
                Selector selector = Selector.open();
                ServerSocketChannel server = ServerSocketChannel.open();
        ) {
            server.bind(new InetSocketAddress("localhost", 8080));
            server.configureBlocking(false);

            server.register(selector, OP_ACCEPT);

            while (true) {
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    //Connection accepted event
                    if (key.isAcceptable()) {
                        SocketChannel connection = server.accept();
                        connection.configureBlocking(false);
                        connection.register(selector, SelectionKey.OP_READ, "Connection 1");
                    }

                    //Data available event
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        StringBuilder response = new StringBuilder("Hello! Server received your message - ");

                        SocketChannel connection = (SocketChannel) key.channel();

                        //read all the data
                        while (connection.read(buffer) > 0) {
                            String message = UTF_8.decode(buffer.flip()).toString();

                            if (message.contains(STOP_SWITCH)) {
                                state = STOP_SWITCH;
                            }

                            response.append(message);
                            buffer.clear();
                        }

                        System.out.println("Server received message from - " + key.attachment());

                        //write response
                        connection.write(ByteBuffer.wrap(response.toString().getBytes()));

                        if (state.equals(STOP_SWITCH)) {
                            connection.close();
                        }
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}