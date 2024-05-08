package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class QuickSort {
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

    private static <E extends Comparable<E>> int choosePivot(E[] array, int start, int end) {
        int middle =start + (end - start )/2;

        //if there are less than 3 elements - return the end index as pivot
        if (middle == start || middle == end) {
            return end;
        }

        //Swap in order the first, middle and last element
        if (!isSmaller(array[start], array[middle])) {
            swap(array, start, middle);
        }

        if (!isSmaller(array[start], array[end])) {
            swap(array, start, end);
        }

        if (!isSmaller(array[middle], array[end])) {
            swap(array, middle, end);
        }

        //Put the middle element next to the last
        swap(array, middle, end - 1);

        return end - 1;
    }

    private static <E extends Comparable<E>> void partition(E[] array, int start, int end) {
        if (start >= end) {
            //The array cannot be split further
            return;
        }

        int pivot = choosePivot(array, start, end);
        int lastSmallerElementIndex = pivot == end ? start - 1 : start;


        for (int index = lastSmallerElementIndex + 1; index < pivot; index++) {
            if (isSmaller(array[index], array[pivot])) {
                lastSmallerElementIndex++;

                swap(array, lastSmallerElementIndex, index);
            }
        }

        lastSmallerElementIndex++;
        swap(array, lastSmallerElementIndex, pivot);

        partition(array, start, lastSmallerElementIndex - 1);
        partition(array, lastSmallerElementIndex + 1, end);
    }

    private static <E extends Comparable<E>> void quickSort(E[] array) {
        partition(array, 0, array.length - 1);
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