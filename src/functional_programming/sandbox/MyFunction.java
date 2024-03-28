package functional_programming.sandbox;

@FunctionalInterface
public interface MyFunction<V, R> {
    R apply(V value);
}
