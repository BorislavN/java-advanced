package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer[] numbers = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        mergeSort(numbers);

        StringBuilder output = new StringBuilder();

        for (Integer n : numbers) {
            output.append(n).append(" ");
        }

        System.out.println(output);
    }

    private static <E extends Comparable<E>> void mergeSort(E[] array) {
        split(array, 0, array.length - 1);
    }

    //End index is inclusive
    private static <E extends Comparable<E>> void split(E[] array, int start, int end) {
        if (start >= end) {
            //We've finished splitting the array
            return;
        }

        int middle = (start + end) / 2;

        //Left
        split(array, start, middle);
        split(array, middle + 1, end);

        merge(array, start, middle, end);
    }

    private static <E extends Comparable<E>> void merge(E[] array, int start, int middle, int end) {
        //If the last element of the left part is smaller than the first element of the right part,
        // the subarray is sorted
        if (array[middle].compareTo(array[middle + 1]) <= 0) {
            return;
        }

        E[] temp = Arrays.copyOf(array, array.length);
        int leftIndex = start;
        int rightIndex = middle + 1;

        for (int index = start; index <= end; index++) {
            //If we have gone through all elements on the left
            if (leftIndex > middle) {
                array[index] = temp[rightIndex++];

                continue;
            }

            //If we have gone through all elements on the right
            if (rightIndex > end) {
                array[index] = temp[leftIndex++];

                continue;
            }

            if (temp[leftIndex].compareTo(temp[rightIndex]) <= 0) {
                array[index] = temp[leftIndex++];
            } else {
                array[index] = temp[rightIndex++];
            }
        }
    }
}