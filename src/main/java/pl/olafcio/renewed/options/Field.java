package pl.olafcio.renewed.options;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Field<T> {
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public Field(Supplier<T> getter, Consumer<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public T get() {
        return getter.get();
    }

    public Class<?> type() {
        return get().getClass();
    }

    @SuppressWarnings("unchecked")
    public void set(Object value) {
        setter.accept((T) value);
    }
}
