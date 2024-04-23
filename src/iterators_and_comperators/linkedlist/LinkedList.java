package iterators_and_comperators.linkedlist;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<Node<T>> {
    private Node<T> start;
    private int size;

    public LinkedList() {
        this.start = null;
        this.size = 0;
    }

    public void add(T value) {
        this.size++;

        if (this.start == null) {
            this.start = new Node<>(value);

            return;
        }

        this.start.setNext(new Node<>(value));
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Node<T>> {
        private Node<T> current;
        private Node<T> last;

        public MyIterator() {
            this.current = start;
            this.last = null;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Node<T> next() {
            if (!this.hasNext()) {
                return null;
            }

            Node<T> output = this.current;
            this.current = this.current.getNext();
            this.last = output;

            return output;
        }

        @Override
        public void remove() {
            if (this.last==null){
                throw new IllegalStateException("Current element is null!");
            }

        //TODO
        }
    }
}
