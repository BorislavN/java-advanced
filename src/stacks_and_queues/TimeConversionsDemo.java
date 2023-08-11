package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeConversionsDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        LocalTime time = LocalTime.of(8, 0, 0);

        int seconds = time.toSecondOfDay();

        System.out.println("Seconds: " + seconds);
        System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        seconds += 3;

        time = LocalTime.ofSecondOfDay(seconds);

        System.out.println("Seconds: " + seconds);
        System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
