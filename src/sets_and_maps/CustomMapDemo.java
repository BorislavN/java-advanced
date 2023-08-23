package sets_and_maps;

import java.util.Objects;

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
        //TODO
    }

    private static class MyMap {
        private MyNode[] array;
        private final int initialCapacity;
        private final double loadThreshold;
        private final double minLoadThreshold;

        private int numberOfOperations;
        private int size;

        public MyMap() {
            this.initialCapacity = 11;
            this.loadThreshold = 0.75;
            this.minLoadThreshold = 0.3;
            this.numberOfOperations = 0;
            this.array = new MyNode[this.initialCapacity];
            this.size = 0;
        }

        public void put(String key, String value) {
            MyNode current = new MyNode(key, value);

            int index = this.getIndex(current.getHash());

            System.out.printf("key: %s val: %s hash: %d%n", key, value, current.getHash());

            MyNode destination = this.array[index];

            if (destination == null) {
                this.array[index] = current;
                this.numberOfOperations++;
                this.size++;
            } else {
                boolean inserted = false;

                while (destination != null) {
                    if (current.getKey().equals(destination.getKey())) {
                        current.setNext(destination.getNext());
                        this.array[index] = current;

                        inserted = true;
                        break;
                    }

                    destination = destination.getNext();
                }

                if (!inserted) {
                    current.setNext(this.array[index]);
                    this.array[index] = current;
                    this.numberOfOperations++;
                    this.size++;
                }
            }

            this.checkForResize();
        }

        public String get(String key) {
            int index = this.getIndex(MyNode.generateHash(key));

            MyNode temp = this.array[index];

            while (temp != null) {
                if (temp.getKey().equals(key)) {
                    return temp.getValue();
                }

                temp = temp.getNext();
            }

            return null;
        }

        public void remove(String key) {
            int index = this.getIndex(MyNode.generateHash(key));
            MyNode prev = null;

            MyNode temp = this.array[index];

            while (temp != null) {
                if (temp.getKey().equals(key)) {

                    if (prev != null) {
                        prev.setNext(temp.getNext());
                    } else {
                        this.array[index] = temp.getNext();
                    }

                    this.size--;
                    this.numberOfOperations++;

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

        private void checkForResize() {
            if (this.numberOfOperations >= 7) {
                double load = this.size * 1.0 / this.array.length;

                if (load >= this.loadThreshold) {
                    resize(Math.max(this.initialCapacity, this.array.length) * 2);
                }

                if (this.array.length > this.initialCapacity && load <= this.minLoadThreshold) {
                    resize(Math.max(this.initialCapacity, this.array.length / 2));
                }
            }
        }

        private void resize(int newSize) {
            MyNode[] temp = new MyNode[newSize];

            for (MyNode node : this.array) {
                if (node != null) {
                    MyNode currentNode = node;

                    while (currentNode != null) {
                        int index = this.getIndex(currentNode.getHash());

                        MyNode copy = new MyNode(currentNode.getKey(), currentNode.getValue());

                        if (temp[index] == null) {
                            temp[index] = copy;
                        } else {
                            MyNode currentInTemp = temp[index];

                            while (true) {
                                if (currentInTemp.getNext() == null) {
                                    currentInTemp.setNext(copy);
                                    break;
                                }

                                currentInTemp = currentInTemp.getNext();
                            }

                        }

                        currentNode = currentNode.getNext();
                    }
                }
            }

            this.array = temp;
            this.numberOfOperations = 0;
            System.out.printf("%nArray resized: new length - %d%n%n", this.array.length);
        }
    }

    private static class MyNode {
        private final int hash;
        private String key;
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
            return Objects.hashCode(key);
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
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
    }
}