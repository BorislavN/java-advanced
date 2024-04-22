package iterators_and_comperators.froggy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] data = reader.readLine().split(",\\s+");
        String command = reader.readLine();
        Lake lake = new Lake(data);

        if ("END".equals(command)) {
            List<String> result = new ArrayList<>();

            lake.forEach(e->result.add(e.toString()));

            System.out.println(String.join(", ",result));
        }
    }
}
