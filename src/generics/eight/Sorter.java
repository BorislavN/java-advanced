package generics.eight;

public class Sorter {
    public static <T extends Comparable<T>> void sort(CustomList<T> list) {
        list.sort();
    }
}
