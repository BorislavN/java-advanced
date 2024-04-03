package functional_programming.sandbox;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<F, S, T, R> {
    R call(F first, S second, T third);

    default Function<S, Function<T, R>> curry(F first) {
        return (second) -> (third) -> this.call(first, second, third);
    }
}