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
        Node<E> newNode = new Node<>(element);
        this.size++;

        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;

            return;
        }

        newNode.setPrev(this.tail);
        this.tail.setNext(newNode);
        this.tail = newNode;
    }

    public void add(int index, E element) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == this.size) {
            this.add(element);

            return;
        }

        Node<E> newNode = new Node<>(element);
        Node<E> target = this.head;
        this.size++;

        while (index-- > 0) {
            target = target.getNext();
        }

        if (target.getPrev() == null) {
            this.head = newNode;
        }

        newNode.setNext(target);
        newNode.setPrev(target.getPrev());

        if (target.getPrev() != null) {
            target.getPrev().setNext(newNode);
        }

        target.setPrev(newNode);
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
        StringBuilder output = new StringBuilder();

        if (this.head != null) {
            Node<E> current = this.head;

            while (current != null) {
                output.append(String.format("Value: %s, Prev: %s, Next %s%n", current.getValue(), current.getPrev(), current.getNext()));
                current = current.getNext();
            }
        }

        return output.toString();
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
