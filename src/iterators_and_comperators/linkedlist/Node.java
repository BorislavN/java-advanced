package iterators_and_comperators.linkedlist;

public class Node<T> {
    private final T value;
    private Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    public T getValue() {
        return this.value;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> val) {
        this.next = val;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
