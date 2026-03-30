package pl.olafcio.renewed.api.registry;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public enum RegistryAccessor {
    ;

    public static <T> List<T> freeze(Registry<T> registry) {
        return registry.freeze();
    }
}
