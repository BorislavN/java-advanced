package generics.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //T[] values=(T[]) new Object[length];
        //The above doesn't work, because we cannot cast an superclass instance to a subclass instance
        String[] one = GenericArray.create(String.class,3);

        one[0] ="Pesho";

        System.out.println(Arrays.toString(one));
    }
}
