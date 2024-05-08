package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//Implementation using Hoare’s Partition
public class QuicksortV2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer[] arr = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        quickSort(arr);

        StringBuilder output = new StringBuilder();

        for (Integer element : arr) {
            output.append(element).append(" ");
        }

        System.out.println(output);
    }

    private static <E extends Comparable<E>> int partition(E[] array, int start, int end) {
        int middle=start + (end - start )/2;
        E pivot = array[middle];

        int leftIndex = start - 1;
        int rightIndex = end + 1;

        while (true) {
            do {
                leftIndex++;
            } while (isSmaller(array[leftIndex], pivot));

            do {
                rightIndex--;
            } while (isSmaller(pivot, array[rightIndex]));

            if (leftIndex >= rightIndex) {
                return rightIndex;
            }

            swap(array, leftIndex, rightIndex);
        }
    }

    private static <E extends Comparable<E>> void sort(E[] array, int start, int end) {
        if (start < end) {
            int pivot = partition(array, start, end);

            sort(array, start, pivot);
            sort(array, pivot + 1, end);
        }
    }

    private static <E extends Comparable<E>> void quickSort(E[] array) {
        sort(array, 0, array.length - 1);
    }

    private static <E extends Comparable<E>> boolean isSmaller(E first, E second) {
        return first.compareTo(second) < 0;
    }

    private static <E> void swap(E[] array, int firstIndex, int secondIndex) {
        if (firstIndex != secondIndex) {
            E temp = array[firstIndex];

            array[firstIndex] = array[secondIndex];
            array[secondIndex] = temp;
        }
    }
}
