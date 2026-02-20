package pl.olafcio.renewed.mixininterface;

public interface IInput {
    boolean isJumping();
    boolean isSneaking();
    boolean isSprinting();

    void setJumping(boolean value);
    void setSneaking(boolean value);
    void setSprinting(boolean value);

    float movementSideways();
    float movementForward();

    void movementSideways(float value);
    void movementForward(float value);
}
