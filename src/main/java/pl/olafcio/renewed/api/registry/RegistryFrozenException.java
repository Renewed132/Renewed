package pl.olafcio.renewed.api.registry;

public class RegistryFrozenException extends RuntimeException {
    public RegistryFrozenException(String message) {
        super(message);
    }
}
