package sets_and_maps;

import java.util.Arrays;
import java.util.Objects;

//Collisions are handled using the double hashing technique
public class CustomMapDemoV2 {
    public static void main(String[] args) {

        MyMap map = new MyMap();

        map.put("key1", "Pesho");
        map.put("key2", "Vanio");
        map.put("key3", "Gosho");
        map.put("key4", "Misho");
        map.put("key1", "Azis");

        System.out.println(map);
        System.out.println();

        System.out.println(map.get("key4"));
        System.out.println(map.get("key1"));
        System.out.println(map.get("key11"));

        map.remove("key4");
        map.remove("key2");
        map.remove("key28");

        System.out.println();
        System.out.println(map);

        map.put("key10", "Azis10");
        map.put("key11", "Azis11");
        map.put("key12", "Azis12");
        map.put("key13", "Azis13");
        map.put("key14", "Azis14");
        map.put("key15", "Azis15");
        map.put("key16", "Azis16");
        map.put("key17", "Azis17");
        map.put("key18", "Azis18");

        System.out.println(map.get("key18"));
        System.out.println(map.get("hahahahah"));

        map.put("key19", "Azis19");
        map.put("key20", "Azis20");
        map.put("key21", "Azis21");
        map.put("key22", "Azis22");
        map.put("key23", "Azis23");
        map.put("key24", "Azis24");
        map.put("key25", "Azis25");
        map.put("key26", "Azis26");
        map.put("key27", "Azis27");

        System.out.println(map);

        map.remove("key10");
        map.remove("key11");
        map.remove("key12");
        map.remove("key13");
        map.remove("key14");
        map.remove("key15");
        map.remove("key16");
        map.remove("key17");
        map.remove("key18");
        map.remove("key19");
        map.remove("key20");
        map.remove("key21");
        map.remove("key22");
        map.remove("key23");

        map.put("key11", "I'm back");

        System.out.println(map);

        System.out.println();
        System.out.println(map.get("key11"));
        System.out.println(map.get("key22"));
        System.out.println(map.get("key27"));
    }

    private static class MyMap {
        private boolean[] primes;
        private MyNode[] array;
        private int size;
        private final int initialCapacity;
        private final double maxLoad;
        private final double minLoad;
        private int numberOfOperations;
        private final int operationsThreshold;

        public MyMap() {
            this.numberOfOperations = 0;
            this.maxLoad = 0.6;
            this.minLoad = 0.2;
            this.initialCapacity = 11;
            this.operationsThreshold = 7;
            this.size = 0;
            this.array = new MyNode[this.initialCapacity];
            this.generatePrimeList(this.initialCapacity * 3);
        }

        public String get(String key) {
            int index = this.indexOfKey(key);

            if (index != -1) {
                return this.array[index].getValue();
            }

            return null;
        }

        public MyNode remove(String key) {
            int index = this.indexOfKey(key);

            if (index != -1) {
                MyNode temp = this.array[index];
                this.array[index] = null;
                this.numberOfOperations++;
                this.size--;

                return temp;
            }

            return null;
        }

        public void put(String key, String value) {
            this.addElement(new MyNode(key, value));
            this.numberOfOperations++;

            checkForResize();
        }

        private void checkForResize() {
            if (this.numberOfOperations > this.operationsThreshold) {
                double load = this.size * 1.0 / this.getCapacity();

                if (load >= this.maxLoad) {
                    if (this.getCapacity() * 2 >= this.primes.length) {
                        this.generatePrimeList(this.getCapacity() * 3);
                    }

                    resize(getNextPrime(this.getCapacity() * 2));

                } else if (this.getCapacity() > this.initialCapacity && load <= this.minLoad) {

                    resize(getNextPrime(Math.max(this.initialCapacity, this.getCapacity() / 2)));
                }
            }
        }

        private void resize(int newSize) {//The array only resizes after additions, if the space is too much or too little
            MyNode[] temp = Arrays.copyOf(this.array, this.array.length);
            this.array = new MyNode[newSize];
            this.numberOfOperations = 0;
            this.size = 0;

            for (MyNode node : temp) {
                if (node != null) {
                    this.addElement(node);
                }
            }

            System.out.printf("%nArray resized: new length - %d%n%n", this.array.length);
        }

        private void addElement(MyNode element) {
            int initialIndex = getIndex(element.getKey());//initialIndex
            int step = getStepNumber(element.getKey());

            int currentIndex = initialIndex;

            while (this.array[currentIndex] != null) {
                if (element.getKey().equals(this.array[currentIndex].getKey())) {//if already exists
                    this.array[currentIndex].setValue(element.getValue());//set new value

                    return;
                }

                currentIndex = getNextIndex(currentIndex, step);//step ahead

                if (initialIndex == currentIndex) {
                    throw new IllegalStateException(String.format("Array full, can't find free cell for key: %s!", element.getKey()));
                }
            }

            this.array[currentIndex] = element;
            size++;
        }

        private int indexOfKey(String key) {
            int initialIndex = getIndex(key);
            int step = getStepNumber(key);

            int currentIndex = initialIndex;

            do {
                if (this.array[currentIndex] != null && key.equals(this.array[currentIndex].getKey())) {
                    return currentIndex;
                }

                currentIndex = getNextIndex(currentIndex, step);

            } while (currentIndex != initialIndex);

            return -1;
        }

        private int getIndex(String key) {
            return MyNode.generateHash(key) % this.getCapacity();
        }

        private int getNextIndex(int index, int step) {
            return (index + step) % this.getCapacity();
        }

        private int getStepNumber(String key) {
            return (MyNode.generateHash(key + "code") % (this.getCapacity() - 1)) + 1;//Here we are modifying the key, to offset the hash
//            return (MyNode.generateHash(key) % (this.getCapacity() - 1)) + 1;//but it works with the standard hash too
        }

        public int getCapacity() {
            return this.array.length;
        }

        public int getSize() {
            return this.size;
        }

        private int getNextPrime(int start) {
            for (int index = start; index < this.primes.length; index++) {
                if (this.primes[index]) {
                    return index;
                }
            }

            return this.initialCapacity;//return the initial capacity as a failsafe;
        }

        private void generatePrimeList(int end) {
            this.primes = new boolean[end + 1];
            Arrays.fill(this.primes, 2, this.primes.length, true);

            for (int number = 2; number * number <= end; number++) {
                if (this.primes[number]) {
                    for (int current = number * 2; current <= end; current += number) {
                        this.primes[current] = false;
                    }
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();
            output.append(String.format("Capacity: %d Size: %d%n", this.getCapacity(), this.getSize()));

            for (MyNode element : this.array) {
                if (element != null) {
                    output.append(element).append(System.lineSeparator());
                }
            }

            return output.toString().trim();
        }
    }

    private static class MyNode {
        private int hash;
        private final String key;
        private String value;

        public MyNode(String key, String value) {
            this.key = key;
            this.value = value;
            this.hash = generateHash(key);
        }

        public int getHash() {
            return this.hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
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

        @Override
        public String toString() {
            return String.format("| Hash: %d Key: %s Value: %s | "
                    , this.getHash()
                    , this.getKey()
                    , this.getValue());
        }
    }
}