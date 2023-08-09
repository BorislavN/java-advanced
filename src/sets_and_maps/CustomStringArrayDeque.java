package sets_and_maps;

import java.util.ArrayList;
import java.util.List;

public class CustomStringArrayDeque {
    public static void main(String[] args) {
        BigDeque queue = new BigDeque();

        queue.offer("One");
        queue.offer("Two");
        queue.offer("Three");

        System.out.println("Queue peek: " + queue.peek());
        System.out.println();

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

        queue.push("One");
        queue.push("Two");
        queue.push("Three");

        System.out.println();
        System.out.println("Stack peek: " + queue.peek());
        System.out.println();

        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
    }

    private static class BigDeque {
        private final List<String> list;

        private BigDeque() {
            list = new ArrayList<>();
        }

        public void push(String element) {
            this.list.add(element);
        }

        public String pop() {
            return this.list.remove(this.list.size() - 1);
        }

        public String peek() {
            return this.list.get(this.list.size() - 1);
        }

        public void offer(String element) {
            this.list.add(0, element);
        }

        public String poll() {
            return this.pop();
        }
    }
}
