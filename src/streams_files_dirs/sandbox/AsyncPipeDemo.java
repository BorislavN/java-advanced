package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AsyncPipeDemo {
    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();

        Thread producer = new Thread(getProducer(pipe, 11));
        Thread consumer = new Thread(getConsumer(pipe,11));

        producer.start();
        consumer.start();

        while (producer.isAlive() || consumer.isAlive()) {
            try {
                Thread.sleep(1000);

                System.out.println("Waiting....");
            } catch (InterruptedException e) {
                System.err.println("Main: Ooops - " + e.getMessage());
            }
        }

        pipe.source().close();
        pipe.sink().close();
    }

    private static Runnable getProducer(Pipe pipe, int limit) {
        return () -> {
            List<String> data = new ArrayList<>();

            for (int i = 1; i <= limit; i++) {
                data.add("message" + i + " ");
            }

            try {
                pipe.sink().configureBlocking(false);

                for (String message : data) {
                    System.out.println("Generating...");

                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(UTF_8));

                    int written = pipe.sink().write(buffer);

                    while (written > 0 && buffer.hasRemaining()) {
                        written = pipe.sink().write(buffer);
                    }
                }
            } catch (IOException e) {
                System.err.println("Producer: Ooops - " + e.getMessage());
            }
        };
    }

    private static Runnable getConsumer(Pipe pipe, int count) {
        return () -> {
            try {
                List<String> messages = new ArrayList<>();

                pipe.source().configureBlocking(false);

                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (messages.size() < count) {
                    int read = pipe.source().read(buffer);

                    while (read > 0) {
                        System.out.println("Receiving...");

                        messages.addAll(
                                Arrays.stream(UTF_8.decode(buffer.flip()).toString().split("\\s+")).toList()
                        );

                        read = pipe.source().read(buffer.clear());
                    }
                }

                messages.forEach(System.out::println);

            } catch (IOException e) {
                System.err.println("Consumer: Ooops - " + e.getMessage());
            }
        };
    }

}
