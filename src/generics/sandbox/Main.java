package generics.sandbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        //T[] values=(T[]) new Object[length];
        //The above doesn't work, because we cannot cast a superclass instance to a subclass instance
        String[] one = GenericArray.create(String.class, 3);
        String[] two = GenericArray.create("pesho", "gosho");
        //Heap Pollution
        Object[] three = GenericArray.create(new ArrayList<String>(), new ArrayList<Double>());


        one[0] = "Pesho";
        System.out.println(Arrays.toString(two));
        two[0] = "Ivooo";

        ArrayList<Integer> first = (ArrayList<Integer>) (three[0]);
        ArrayList<String> second = (ArrayList<String>) (three[1]);
        ArrayList<Double> third = (ArrayList<Double>) (three[0]);

        first.add(5);

        second.add("pesho");

        third.add(5.55);

        System.out.println(Arrays.toString(three));
        System.out.println(Arrays.toString(one));
        System.out.println(Arrays.toString(two));
        //This will throw an exception
        System.out.println(((ArrayList<String>) (three[0])).get(0));
    }
}
