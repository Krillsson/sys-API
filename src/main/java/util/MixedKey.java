package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MixedKeySerializer.class)
public class MixedKey<T> {
    public final Class<T> cls;
    public final String key;

    public MixedKey(Class<T> cls, String key) {
        this.key = key;
        this.cls = cls;
    }

    public Class<T> getCls() {
        return cls;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MixedKey mixedKey = (MixedKey) o;

        if (!cls.equals(mixedKey.cls)) return false;
        if (!key.equals(mixedKey.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cls.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

}
