package functional_programming.sandbox;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> sum = (v) -> v + 10;
        Function<Integer, Integer> multiply = (v) -> v * 2;
        Function<Integer, Integer> divide = (v) -> v / 2;

        int result = multiply.andThen(sum).andThen(divide).apply(10);

        System.out.printf("Standard function chaining, result: %d%n", result);

        TriFunction<Integer, Integer, Integer, Integer> triSum = (first, second, third) -> first + second + third;

        int secondResult = triSum.call(3, 4, 5);

        System.out.printf("TriFunction call, result: %d%n", secondResult);

        Function<Integer, Function<Integer, Integer>> partial = triSum.curry(5);
        Integer thirdResult = partial.apply(4).apply(3);

        System.out.printf("TriFunction currying, result: %d%n", thirdResult);

        MyFunction<Integer, Integer> sumV2 = (v) -> v + 5;
        MyFunction<Integer, Integer> multiplyV2 = (v) -> v * 2;

        int thenResult = sumV2.then(multiplyV2).call(1);
        int beforeResult = sumV2.compose(multiplyV2).call(1);

        System.out.printf("MyFunction then example, result: %d%n", thenResult);
        System.out.printf("MyFunction compose example, result: %d%n", beforeResult);
    }
}