package iterators_and_comperators.sandbox;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        if (index == this.size) {
            this.add(element);

            return;
        }

        this.validateIndex(index);

        Node<E> newNode = new Node<>(element);
        Node<E> target = this.getNode(index);
        this.size++;

        newNode.setNext(target);
        newNode.setPrev(target.getPrev());

        if (target.getPrev() != null) {
            target.getPrev().setNext(newNode);
        } else {
            this.head = newNode;
        }

        target.setPrev(newNode);
    }

    public void remove(E element) {
        Iterator<E> iterator = this.iterator();

        while (iterator().hasNext()) {
            E current = iterator.next();

            if (current.equals(element)) {
                iterator.remove();

                break;
            }
        }
    }

    public E remove(int index) {
        Node<E> target = this.getNode(index);
        this.removeNode(target);

        return target.getValue();
    }

    public boolean contains(E element) {
        for (E e : this) {
            if (e.equals(element)) {
                return true;
            }
        }

        return false;
    }

    public E get(int index) {
        return this.getNode(index).getValue();
    }

    public E set(int index, E element) {
        Node<E> current = this.getNode(index);
        E oldValue = current.getValue();

        current.setValue(element);

        return oldValue;
    }

    public int getSize() {
        return this.size;
    }

    @SuppressWarnings("unchecked")
    public E[] toArray() {
        if (this.head == null) {
            return (E[]) new Object[0];
        }

        E[] temp = (E[]) Array.newInstance(this.head.getValue().getClass(), this.size);
        Node<E> start = this.head;

        for (int index = 0; index < temp.length; index++) {
            temp[index] = start.getValue();
            start = start.getNext();
        }

        return temp;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
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

        return output.toString().trim();
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private Node<E> getNode(int index) {
        this.validateIndex(index);
        Node<E> current = this.head;

        if (index <= this.size / 2) {
            while (index-- > 0) {
                current = current.getNext();
            }

            return current;
        }

        current = this.tail;
        index = this.size - 1 - index;

        while (index-- > 0) {
            current = current.getPrev();
        }


        return current;
    }

    private void removeNode(Node<E> node) {
        Node<E> prev = node.getPrev();
        Node<E> next = node.getNext();

        if (prev != null) {
            prev.setNext(next);
        } else {
            this.head = next;
        }

        if (next != null) {
            next.setPrev(prev);
        } else {
            this.tail = prev;
        }

        this.size--;
    }

    private class MyIterator implements Iterator<E> {
        private Node<E> current;
        private Node<E> last;

        public MyIterator() {
            this.current = head;
            this.last = null;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("No next element!");
            }
            this.last = this.current;
            this.current = this.current.getNext();

            return this.last.getValue();
        }

        @Override
        public void remove() {
            if (this.last == null) {
                throw new IllegalStateException("next() must be called, before removing the element!");
            }

            removeNode(this.last);
            this.last = null;
        }
    }
}
