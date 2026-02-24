package pl.olafcio.renewed.mixininterface;

public interface IMaterial {
    boolean isFluid();

    default boolean canBeNaturallyReplacedWithBlock() {
        return isFluid();
    }
}
