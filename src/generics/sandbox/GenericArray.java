package generics.sandbox;

import java.lang.reflect.Array;

public class GenericArray {

    public static <T> T[] create(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }
}
