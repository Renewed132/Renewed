package pl.olafcio.renewed.api.internal;

import pl.olafcio.renewed.Renewed;
import pl.olafcio.renewed.api.registry.RegistryAccessor;

public enum ItemFinalization {
    ;

    public static void introduce() {
        RegistryAccessor.freeze(Renewed.Items);
        // Items auto-register in the Item constructor
    }
}
