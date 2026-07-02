package pl.olafcio.renewed.mixininterface;

public interface IMaterial {
    boolean __isFluid();

    default boolean canBeNaturallyReplacedWithBlock() {
        return __isFluid();
    }
}
