package iterators_and_comperators.listyiterator;

import java.util.Iterator;
import java.util.List;

public class ListyIterator implements Iterable<String> {
    private final List<String> list;
    private int index;

    public ListyIterator(List<String> list) {
        this.list = list;
        this.index = 0;
    }

    public boolean move() {
        if (this.hasNext()) {
            this.index++;

            return true;
        }
        return false;
    }

    public boolean hasNext() {
        return this.index < this.list.size() - 1;
    }

    public void print() {
        if (this.list.isEmpty()) {
            throw new IllegalStateException("Invalid Operation!");
        }

        System.out.println(this.list.get(this.index));
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return this.position < list.size();
            }

            @Override
            public String next() {
                return list.get(this.position++);
            }
        };
    }
}
