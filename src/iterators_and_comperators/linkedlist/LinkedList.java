package iterators_and_comperators.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<Node<T>> {
    private Node<T> start;
    private Node<T> end;
    private int size;

    public LinkedList() {
        this.start = null;
        this.end = null;
        this.size = 0;
    }

    public int getSize() {
        return this.size;
    }

    //Adding elements while iterating does not guarantee that the loop will include the new elements
    //this is because the internal iterator pointer will not be updated in some cases
    //Like if we are at the end of the loop and a new element is added
    public void add(T value) {
        this.size++;

        Node<T> newNode = new Node<>(value);

        if (this.start == null) {
            this.start = newNode;
            this.end = this.start;

            return;
        }

        this.end.setNext(newNode);
        this.end = newNode;
    }

    public void remove(T element) {
        Iterator<Node<T>> iter = this.iterator();

        while (iter.hasNext()) {
            Node<T> current = iter.next();

            if (current.getValue().equals(element)) {
                iter.remove();

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
        private boolean removeCalled;


        public MyIterator() {
            this.prevElement = null;
            this.currentElement = null;
            this.nextElement = start;
            this.removeCalled = false;
        }

        @Override
        public boolean hasNext() {
            return this.nextElement != null;
        }

        @Override
        public Node<T> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("No next element!");
            }

            Node<T> output = this.nextElement;

            this.prevElement = this.currentElement;
            this.currentElement = output;
            this.nextElement = output.getNext();

            this.removeCalled = false;

            return output;
        }

        @Override
        public void remove() {
            if (this.currentElement == null) {
                throw new IllegalStateException("Current element is null!");
            }

            if (this.removeCalled) {
                throw new IllegalStateException("The element was already removed!");
            }

            //Reset "start" reference if the "currentElement" is the first in the list
            if (this.prevElement == null) {
                start = this.nextElement;
            }

            //Reset "end" reference if the "currentElement" is the last in the list
            if (this.nextElement == null) {
                end = this.prevElement;
            }

            //Attach the "nextElement" as the child of the "prevElement"
            if (this.prevElement != null) {
                this.prevElement.setNext(this.nextElement);
            }

            //Remove the reference to the deleted element
            this.currentElement = this.prevElement;

            this.removeCalled = true;
            size--;
        }
    }
}
