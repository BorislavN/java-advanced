package iterators_and_comperators.froggy;

import java.util.Arrays;
import java.util.Iterator;

public class Lake implements Iterable<Integer> {
    private final int[] numbers;

    public Lake(String[] numbers) {
        this.numbers = Arrays.stream(numbers)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Frog();
    }

    public class Frog implements Iterator<Integer> {
        private int position;

        public Frog() {
            this.position = 0;
        }

        @Override
        public boolean hasNext() {
            return this.position < numbers.length;
        }

        @Override
        public Integer next() {
            if (!this.hasNext()) {
                return null;
            }

            int element = numbers[this.position];
            this.position += 2;

            if (this.position >= numbers.length) {
                if (this.position % 2 == 0) {
                    this.position = 1;
                }
            }

            return element;
        }
    }
}
