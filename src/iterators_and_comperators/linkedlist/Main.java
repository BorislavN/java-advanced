package iterators_and_comperators.linkedlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        LinkedList<Integer> list = new LinkedList<>();

        while (n-- > 0) {
            String[] command = reader.readLine().split("\\s+");

            if ("Add".equals(command[0])) {
                list.add(Integer.parseInt(command[1]));
            }

            if ("Remove".equals(command[0])) {
                list.remove(Integer.parseInt(command[1]));
            }
        }

        System.out.println(list.getSize());
        list.forEach(e-> System.out.print(e.toString().concat(" ")));
    }
}
