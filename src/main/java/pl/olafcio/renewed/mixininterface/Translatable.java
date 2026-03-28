package pl.olafcio.renewed.mixininterface;

public interface Translatable {
    String __getTranslationKey();

    default String getUseKey() {
        return __getTranslationKey();
    }
}
