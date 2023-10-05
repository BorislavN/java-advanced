package streams_files_dirs.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

//Java NIO Pipe demo
public class PipeDemo {
    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();

        Thread producer= new Thread(getProducer(pipe,10));
        Thread consumer= new Thread(getConsumer(pipe));

        producer.start();
        consumer.start();

        while (producer.isAlive()||consumer.isAlive()){
            try {
                Thread.sleep(1000);

                System.out.println("Waiting....");
            } catch (InterruptedException e) {
                System.err.println("Main: Ooops - "+e.getMessage());
            }
        }

        pipe.source().close();
        pipe.sink().close();
    }

    private static Runnable getProducer(Pipe pipe, int limit) {
        return () -> {
            List<String> data = new ArrayList<>();

            for (int i = 1; i <= limit; i++) {
                data.add("message" + i+" ");
            }

            try {
                //Simulate work
                System.out.println("Generating...");
                Thread.sleep(2000);

                pipe.sink().write(data.stream()
                        .map(e -> ByteBuffer.wrap(e.getBytes(UTF_8)))
                        .toArray(ByteBuffer[]::new)
                );
            } catch (InterruptedException | IOException e) {
                System.err.println("Producer: Ooops - " + e.getMessage());
            }
        };
    }

    private static Runnable getConsumer(Pipe pipe) {
        return () -> {
            try {
                System.out.println("Receiving...");
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                int read = pipe.source().read(buffer);

                if (read>0){
                    for (String element : UTF_8.decode(buffer.flip()).toString().split("\\s+")) {
                        System.out.println(element);
                    }
                }
            } catch (IOException e) {
                System.err.println("Consumer: Ooops - " + e.getMessage());
            }
        };
    }
}