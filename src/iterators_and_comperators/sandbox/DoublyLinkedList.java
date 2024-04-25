package iterators_and_comperators.sandbox;

import java.util.Iterator;

public class DoublyLinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(E element) {
        //TODO: this method should add element to the end of the list
    }

    public void add(int index, E element) {
        //TODO: this method should add element at the given index
        // we won't have index field, but a variable counting nodes in a loop
    }

    public E remove(E element) {
        //TODO: this method should remove the first element matching the value
        return null;
    }

    public E remove(int index) {
        //TODO: this method should remove the element at the given position
        return null;
    }

    public boolean contains(E element) {
        //TODO: this method should return true if the element is present
        return false;
    }

    public E get(int index) {
        //TODO: this method should return the element at the given index
        return null;
    }

    public E set(int index, E element) {
        //TODO: this method should set new value at the given index
        // and return the old value
        return null;
    }

    public int getSize() {
        return this.size;
    }

    public E[] toArray() {

        //TODO: this method should return an array resembling the list, crated with Array.newInstance(this.tail.getValue().getClass(),size)
        return null;
    }

    @Override
    public String toString() {
        //TODO: this method should return a String.format of all elements, separated by ", "
        return "DoublyLinkedList{}";
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }
}
