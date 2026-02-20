package pl.olafcio.renewed.mixin;

import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import pl.olafcio.renewed.mixininterface.IInput;

@Mixin(Input.class)
@SuppressWarnings("all")
public class InputMixin implements IInput {
    @Shadow public float movementSideways;
    @Shadow public float movementForward;

    @Shadow public boolean jumping;
    @Shadow public boolean sneaking;
    @Unique public boolean sprinting = false;

    @Override
    public boolean isJumping() {
        return jumping;
    }

    @Override
    public boolean isSneaking() {
        return sneaking;
    }

    @Override
    public boolean isSprinting() {
        return sprinting;
    }

    @Override
    public void setJumping(boolean value) {
        jumping = value;
    }

    @Override
    public void setSneaking(boolean value) {
        sneaking = value;
    }

    @Override
    public void setSprinting(boolean value) {
        sprinting = value;
    }

    @Override
    public float movementSideways() {
        return movementSideways;
    }

    @Override
    public float movementForward() {
        return movementForward;
    }

    @Override
    public void movementSideways(float value) {
        movementSideways = value;
    }

    @Override
    public void movementForward(float value) {
        movementForward = value;
    }
}
