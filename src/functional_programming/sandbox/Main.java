package functional_programming.sandbox;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Integer,Integer> sum=(v)->v+10;
        Function<Integer,Integer> multiply=(v)->v*2;
        Function<Integer,Integer> divide=(v)->v/2;

      int result=  multiply.andThen(sum).andThen(divide).apply(10);

        System.out.println(result);
    }
}
