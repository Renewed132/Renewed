package pl.olafcio.renewed.options;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FieldMap extends HashMap<String, Field<?>> {
    public <T> Field<T> put(String key, Supplier<T> getter, Consumer<T> setter) {
        Field<T> field = new Field<>(getter, setter);
        super.put(key, field);
        return field;
    }
}
