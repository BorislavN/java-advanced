package iterators_and_comperators.sandbox;

public class Main {
    public static void main(String[] args) {
        DoublyLinkedList<String> list=new DoublyLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
//        list.add(0,"pesho");
//        list.add(1,"gosho");
//        list.add(2,"ivan");
        list.add(3,"stamat");

        System.out.println(list);
    }
}
