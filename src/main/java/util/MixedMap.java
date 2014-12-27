package util;

import java.util.*;

public class MixedMap extends HashMap<MixedKey<?>,ArrayList> {
    public <T> ArrayList<T> putMixed(MixedKey<T> key, ArrayList<T> value) {
        return put(key, value);
    }

    public <T> ArrayList<T> getMixed(MixedKey<T> key) {
        return get(key);
    }
}
