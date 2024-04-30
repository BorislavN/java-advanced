package iterators_and_comperators.sandbox;

public class Main {
    public static void main(String[] args) {
        DoublyLinkedList<String> list=new DoublyLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add(0,"0.5");
        list.add(2,"1.5");
        list.add(4,"2.5");
        list.add(6,"3.5");

        System.out.println(list);
    }
}
