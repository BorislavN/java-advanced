package iterators_and_comperators.linkedlist;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<Node<T>> {
    private Node<T> start;
    private int size;

    public LinkedList() {
        this.start = null;
        this.size = 0;
    }

    public int getSize() {
        return this.size;
    }

    public void add(T value) {
        this.size++;

        if (this.start == null) {
            this.start = new Node<>(value);

            return;
        }

        Node<T> last = this.start;
        Iterator<Node<T>> iter = this.iterator();

        while (iter.hasNext()) {
            last = iter.next();
        }

        last.setNext(new Node<>(value));
    }

    public void remove(T element) {
        Iterator<Node<T>> iter = this.iterator();

        while (iter.hasNext()) {
            Node<T> current = iter.next();

            if (current.getValue().equals(element)) {
                iter.remove();
                this.size--;

                break;
            }
        }
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Node<T>> {
        private Node<T> prevElement;
        private Node<T> currentElement;
        private Node<T> nextElement;

        public MyIterator() {
            this.prevElement = null;
            this.currentElement = null;
            this.nextElement = start;
        }

        @Override
        public boolean hasNext() {
            return this.nextElement != null;
        }

        @Override
        public Node<T> next() {
            if (!this.hasNext()) {
                return null;
            }

            Node<T> output = this.nextElement;

            this.prevElement = this.currentElement;
            this.currentElement = output;
            this.nextElement = output.getNext();

            return output;
        }

        @Override
        public void remove() {
            if (this.currentElement == null) {
                throw new IllegalStateException("Current element is null!");
            }

            if (this.prevElement != null) {
                this.prevElement.setNext(this.nextElement);
            }

            if (this.prevElement == null) {
                start = this.nextElement;
            }

            this.currentElement = this.nextElement;

            if (this.currentElement!=null){
                this.nextElement = this.currentElement.getNext();
            }
        }
    }
}
