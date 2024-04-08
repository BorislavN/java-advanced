package functional_programming.sandbox;

@FunctionalInterface
public interface MyFunction<T, R> {
    R call(T value);

    default <V> MyFunction<T, V> then(MyFunction<R, V> after) {
        return (T value) -> after.call(this.call(value));
    }

    default <V> MyFunction<V, R> compose(MyFunction<V, T> before) {
        return (V value) -> this.call(before.call(value));
    }
}