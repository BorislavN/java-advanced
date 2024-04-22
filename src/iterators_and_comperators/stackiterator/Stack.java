package iterators_and_comperators.stackiterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stack<T> implements Iterable<T> {
    private final List<T> list;

    public Stack() {
        this.list = new ArrayList<>();
    }

    public T pop() {
        if (this.list.isEmpty()) {
            return null;
        }

        return this.list.remove(this.list.size() - 1);
    }

    public void push(T element) {
        this.list.add(element);
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }

    private class StackIterator implements Iterator<T> {
        private int position;

        public StackIterator() {
            this.position = list.size() - 1;
        }

        @Override
        public boolean hasNext() {
            return this.position >= 0;
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                return list.get(this.position--);
            }

            return null;
        }
    }
}
