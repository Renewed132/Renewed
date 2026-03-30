package pl.olafcio.renewed.api.registry;

import java.util.ArrayList;
import java.util.List;

public final class Registry<T> {
    private final ArrayList<T> entries
            = new ArrayList<>();

    private boolean frozen
            = false;

    public T register(T t) {
        if (frozen)
            throw new RegistryFrozenException("Registry already frozen; cannot register new entries");

        entries.add(t);
        return t;
    }

    List<T> freeze() {
        frozen = true;
        return entries;
    }
}
