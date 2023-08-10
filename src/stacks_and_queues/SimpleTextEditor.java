package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class SimpleTextEditor {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int lines = Integer.parseInt(reader.readLine());

        Deque<String> operations = new ArrayDeque<>();
        StringBuilder output = new StringBuilder();

        while (lines-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            //judge has an old JDK version, the new style switch throws an error

            switch (data[0]) {
                case "1" -> operations.push("add " + appendText(output, data[1]));
                case "2" -> operations.push("remove " + removeText(output, Integer.parseInt(data[1])));
                case "3" -> System.out.println(output.charAt(Integer.parseInt(data[1]) - 1));
                case "4" -> {
                    String[] operation = operations.pop().split("\\s+");

                    if ("add".equals(operation[0])) {
                        removeText(output, operation[1].length());
                    } else {
                        appendText(output, operation[1]);
                    }
                }
            }
        }
    }

    private static int getStartIndex(int length, int count) {
        return Math.max(0, length - count);
    }

    private static String appendText(StringBuilder builder, String text) {
        builder.append(text);

        return text;
    }

    private static String removeText(StringBuilder builder, int count) {
        int index = getStartIndex(builder.length(), count);
        String temp = builder.substring(index);
        builder.setLength(index);

        return temp;
    }
}