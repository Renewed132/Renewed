package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow protected abstract void drawSlot(Slot slot);

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/inventory/slot/Slot;)V"),
            method = "render"
    )
    public void drawSlot(HandledScreen instance, Slot slot) {}

    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;isPointOverSlot(Lnet/minecraft/inventory/slot/Slot;II)Z"),
            method = "render"
    )
    public boolean isSlotHovered(HandledScreen instance, Slot slot, int pointX, int pointY, Operation<Boolean> original) {
        boolean val = original.call(instance, slot, pointX, pointY);
        if (!val)
            this.drawSlot(slot);

        return val;
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V",
                    ordinal = 2,
                    shift = At.Shift.AFTER
            ),
            method = "render"
    )
    public void render__hoverFinished(
            CallbackInfo ci,
            @Local(type = Slot.class, ordinal = 0, index = 2) Slot slot
    ) {
        this.drawSlot(slot);
    }
}
