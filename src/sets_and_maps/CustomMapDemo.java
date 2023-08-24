package sets_and_maps;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

//Collisions are resolved by chaining the elements in a linked list
public class CustomMapDemo {
    public static void main(String[] args) {

        MyMap map = new MyMap();

        map.put("key1", "Pesho");
        map.put("key1", "Sasho");
        map.put("key3", "Pesho");
        map.put("key2", "Gosho");

        System.out.println(map.get("key1"));
        System.out.println(map.get("key2"));
        System.out.println(map.get("key3"));

        map.remove("key1");
        System.out.println(map.get("key1"));

        map.put("key5", "Gosho5");
        map.put("key6", "Gosho6");
        map.put("key7", "Gosho7");
        map.put("key8", "Gosho8");
        map.put("key9", "Gosho9");
        map.put("key10", "Gosho10");
        map.put("key11", "Gosho11");
        map.put("key12", "Gosho12");
        map.put("key13", "Gosho13");
        map.put("key14", "Gosho14");
        map.put("key15", "Gosho15");

        System.out.println(map.get("key2"));
        System.out.println(map.get("key3"));
        System.out.println(map.get("key15"));

        System.out.println(map);

        map.remove("key5");
        map.remove("key6");
        map.remove("key7");
        map.remove("key8");
        map.remove("key9");
        map.remove("key10");
        map.remove("key11");
        map.remove("key12");
        map.remove("key13");
        map.remove("key14");

        System.out.println(map.getSize());
        System.out.println(map);
    }

    private static class MyMap {
        private MyNode[] array;
        private int numberOfOperations;
        private int size;

        public MyMap() {
            this.numberOfOperations = 0;
            this.array = new MyNode[11];
            this.size = 0;
        }

        public void put(String key, String value) {
            this.addElement(new MyNode(key, value));

            this.checkForResize();
        }

        private void addElement(MyNode node) {
            int index = this.getIndex(node.getHash());

            boolean isModified = false;

            if (this.array[index] != null) {
                MyNode temp = this.array[index];

                while (temp != null) {
                    if (node.getKey().equals(temp.getKey())) {//if the key already exists
                        temp.setValue(node.getValue());//we modify the value
                        isModified = true;

                        break;
                    }

                    temp = temp.getNext();
                }
            }

            if (!isModified) {
                node.setNext(this.array[index]);
                this.array[index] = node;

                this.numberOfOperations++;
                this.size++;
            }
        }

        public String get(String key) {
            int index = this.getIndex(MyNode.generateHash(key));
            MyNode rootNode = this.array[index];

            while (rootNode != null) {
                if (rootNode.getKey().equals(key)) {
                    return rootNode.getValue();
                }

                rootNode = rootNode.getNext();
            }

            return null;
        }

        public void remove(String key) {
            int index = this.getIndex(MyNode.generateHash(key));
            MyNode temp = this.array[index];
            MyNode prev = null;

            while (temp != null) {
                if (temp.getKey().equals(key)) {
                    if (prev != null) {
                        prev.setNext(temp.getNext());
                    } else {
                        this.array[index] = temp.getNext();
                    }

                    this.numberOfOperations++;
                    this.size--;

                    break;
                }

                prev = temp;
                temp = temp.getNext();
            }

            this.checkForResize();
        }

        private int getIndex(int hash) {
            return hash % this.array.length;
        }

        public int getSize() {
            return this.size;
        }

        private int getCapacity() {
            return this.array.length;
        }

        private void checkForResize() {
            if (this.numberOfOperations > 7) {
                double load = this.size * 1.0 / this.array.length;

                if (load > 0.75) {
                    resize(Math.max(11, this.array.length) * 2);
                } else if (load < 0.25) {
                    resize(Math.max(11, this.array.length / 2));
                }
            }
        }

        private void resize(int newSize) {
            Deque<MyNode> stack = new ArrayDeque<>();

            for (MyNode node : this.array) {
                if (node != null) {
                    MyNode currentNode = node;

                    while (currentNode != null) {
                        stack.push(new MyNode(currentNode.getKey(), currentNode.getValue()));//load a copy of the current node to the stack
                        currentNode = currentNode.getNext();
                    }
                }
            }

            this.array = new MyNode[newSize];
            stack.forEach(this::addElement);

            this.numberOfOperations = 0;
            size = stack.size();

            System.out.printf("%nArray resized: new length - %d%n%n", this.array.length);
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();
            output.append(String.format("Capacity: %d Size: %d%n", this.getCapacity(), this.getSize()));

            for (MyNode node : this.array) {
                MyNode temp = node;

                while (temp != null) {
                    output.append(temp).append(System.lineSeparator());

                    temp = temp.getNext();
                }
            }

            return output.toString().trim();
        }
    }

    private static class MyNode {
        private final int hash;
        private final String key;
        private String value;
        private MyNode next;

        public MyNode(String key, String value) {
            this.key = key;
            this.value = value;
            this.hash = generateHash(key);
            this.next = null;
        }

        public int getHash() {
            return this.hash;
        }

        public static int generateHash(String key) {
            return Math.abs(Objects.hashCode(key));
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public MyNode getNext() {
            return this.next;
        }

        public void setNext(MyNode next) {
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();
            MyNode temp = this;

            while (temp != null) {
                output.append(String.format("| Hash: %d Key: %s Value: %s | "
                        , temp.getHash()
                        , temp.getKey()
                        , temp.getValue())
                );

                temp = temp.getNext();
            }


            return output.toString().trim();
        }
    }
}