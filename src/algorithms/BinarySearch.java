package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer[] arr = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        System.out.println(getIndex(arr, Integer.parseInt(reader.readLine())));
    }

    private static <E extends Comparable<E>> int getIndex(E[] array, E key) {
        return search(array, key, 0, array.length - 1);
    }

    private static <E extends Comparable<E>> int search(E[] array, E key, int start, int end) {
        if (start <= end) {
            int middle = start + (end - start) / 2;

            if (array[middle].equals(key)) {
                return middle;
            }

            if (key.compareTo(array[middle]) < 0) {
                return search(array, key, start, middle - 1);
            }

            if (key.compareTo(array[middle]) > 0) {
                return search(array, key, middle + 1, end);
            }
        }

        return -1;
    }
}