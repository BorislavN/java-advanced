package generics.nine;

import java.util.Arrays;
import java.util.Iterator;

public class CustomList<T extends Comparable<T>> implements Iterable<T> {
    private Object[] values;
    private int size;

    public CustomList() {
        this.values = new Object[10];
        this.size = 0;
    }

    public void add(T element) {
        if (this.values.length == this.size) {
            this.values = Arrays.copyOf(this.values, this.size * 2);
        }

        this.values[size] = element;
        this.size++;
    }

    public T remove(int index) {
        if (!this.isInBounds(index)) {
            return null;
        }

        T temp = (T) this.values[index];
        this.size--;

        if (this.size < this.values.length / 3) {
            this.values = Arrays.copyOfRange(this.values, 0, this.values.length / 2);
        }

        for (int i = index; i < this.values.length - 1; i++) {
            this.values[i] = this.values[i + 1];
        }

        return temp;
    }

    public boolean contains(T element) {
        for (Object value : this.values) {
            if (element != null && element.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public void swap(int one, int two) {
        if (this.isInBounds(one) && this.isInBounds(two)) {
            T temp = (T) this.values[one];

            this.values[one] = this.values[two];
            this.values[two] = temp;
        }
    }

    public int countGreaterThan(T element) {
        return (int) Arrays.stream(this.values).filter(e -> e != null && element.compareTo((T) e) < 0).count();
    }

    public T max() {
        T max = null;

        for (int i = 0; i < this.size; i++) {
            T current = (T) this.values[i];

            if (max == null) {
                max = current;

                continue;
            }

            if (current.compareTo(max) > 0) {
                max = current;
            }
        }

        return max;
    }

    public T min() {
        T min = null;

        for (int i = 0; i < this.size; i++) {
            T current = (T) this.values[i];

            if (min == null) {
                min = current;

                continue;
            }

            if (current.compareTo(min) < 0) {
                min = current;
            }
        }

        return min;
    }

    public void sort() {
        Arrays.sort(this.values, (e1, e2) -> {
            if (e1 == null) {
                return 1;
            }

            return ((T) e1).compareTo((T) e2);
        });
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < this.size; i++) {
            output.append(this.values[i]);
            output.append(System.lineSeparator());
        }

        return output.toString().trim();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return this.position < size;
            }

            @Override
            public T next() {
                if (this.hasNext()) {
                    return (T) values[this.position++];
                }

                return null;
            }
        };
    }

    private boolean isInBounds(int index) {
        return index >= 0 && index < this.size;
    }
}
