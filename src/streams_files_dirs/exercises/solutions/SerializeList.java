package streams_files_dirs.exercises.solutions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class SerializeList {
    public static void main(String[] args) {
        Path out = Path.of("src/streams_files_dirs/exercises/resources/outputList.ser");

        List<Double> list = Stream.iterate(1d, (x) -> x + 1).limit(10).toList();

        try (
                ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(out, TRUNCATE_EXISTING, CREATE));
                ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(out));
        ) {
            outputStream.writeObject(list);
            outputStream.flush();

            Object loaded = inputStream.readObject();

            //the type is preserved during serialization
            System.out.println(loaded instanceof List<?>);
            System.out.println(loaded instanceof HttpClient);

            System.out.println("Original: " + list);
            System.out.println("Loaded: " + loaded);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}