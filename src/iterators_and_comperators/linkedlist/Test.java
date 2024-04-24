package iterators_and_comperators.linkedlist;

import java.util.Iterator;

public class Test {
    public static void main(String[] args) {
        LinkedList<Integer>list =new LinkedList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        Iterator<Node<Integer>> iterator = list.iterator();

        while (iterator.hasNext()){
            Node<Integer> current = iterator.next();

            System.out.println("Current element - "+current);

            if (current.getValue()==2){
                iterator.remove();

                System.out.println("Removed element - "+current);
            }

            if (current.getValue()==3){
                iterator.remove();
                list.add(4);
                System.out.println("Removed element - "+current);
            }


        }

        System.out.println();
        System.out.println("Size: "+list.getSize());
        System.out.println("Final result:");

        list.forEach(System.out::println);
    }
}
