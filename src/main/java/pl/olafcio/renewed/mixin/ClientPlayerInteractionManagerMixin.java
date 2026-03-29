package pl.olafcio.renewed.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.SwordItem;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private GameMode gameMode;
    @Shadow @Final private Minecraft field_1646;

    @Inject(at = @At("HEAD"), method = "method_1223", cancellable = true)
    public void breakBlock__isCreative(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
             cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;isCreative()Z"), method = "method_1235", cancellable = true)
    public void startBreakingBlock__isCreative(int x, int y, int z, int side, CallbackInfo ci) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1239", cancellable = true)
    public void doBreakBlock__isCreative(int x, int y, int z, int side, CallbackInfo ci) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
            ci.cancel();
        }
    }
}
