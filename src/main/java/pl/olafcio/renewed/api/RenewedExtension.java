package pl.olafcio.renewed.api;

public interface RenewedExtension {
    void onPrepare();
    default void onReady() {}
}
