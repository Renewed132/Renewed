package pl.olafcio.renewed.mixin;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.mixininterface.IGameOptions;
import pl.olafcio.renewed.mixininterface.IInput;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin implements IInput {
    @Shadow
    private GameOptions options;

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tickHEAD(CallbackInfo ci) {
        if (Keyboard.isKeyDown(61 /* F3 */))
            ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tickTAIL(CallbackInfo ci) {
        if (((IGameOptions) this.options).sprintKey().pressed)
            setSprinting(!inPlace());
        else if (inPlace())
            setSprinting(false);

        if (isSprinting()) {
            this.movementSideways((float) ((double) this.movementSideways() * 1.3));
            this.movementForward((float) ((double) this.movementForward() * 1.3));
        }
    }

    @Unique
    private boolean inPlace() {
        return movementForward() < 1 && movementSideways() < 1;
    }
}
