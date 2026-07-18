package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.olafcio.renewed.mixininterface.IClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager  {
    @Shadow private GameMode gameMode;
    @Shadow @Final private Minecraft field_1646;

    @Inject(at = @At("HEAD"), method = "method_1223", cancellable = true)
    public void breakBlock__isCreative(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack() != null &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
             cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;isCreative()Z"), method = "method_1235", cancellable = true)
    public void startBreakingBlock__isCreative(int x, int y, int z, int side, CallbackInfo ci) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack() != null &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1239", cancellable = true)
    public void doBreakBlock__isCreative(int x, int y, int z, int side, CallbackInfo ci) {
        if (
                this.gameMode.isCreative() &&
                this.field_1646.playerEntity.inventory.getMainHandStack() != null &&
                this.field_1646.playerEntity.inventory.getMainHandStack().getItem() instanceof SwordItem
        ) {
            ci.cancel();
        }
    }

    @Unique
    private boolean spectatorMode = false;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isSpectatorMode() {
        return spectatorMode;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;gameModeAbilities(Lnet/minecraft/entity/player/PlayerAbilities;)V", shift = At.Shift.AFTER), method = "copyAbilities")
    public void copyAbilities(PlayerEntity player, CallbackInfo ci) {
        if (spectatorMode)
            player.abilities.invulnerable = true;
    }

    @Inject(at = @At("HEAD"), method = "hasStatusBars", cancellable = true)
    public void hasStatusBars(CallbackInfoReturnable<Boolean> cir) {
        if (spectatorMode)
            cir.setReturnValue(false);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;attack(Lnet/minecraft/entity/Entity;)V"), method = "attackEntity")
    public void attackEntity__attack(PlayerEntity instance, Entity entity, Operation<Void> original) {
        if (!spectatorMode)
            original.call(instance, entity);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_3215(Lnet/minecraft/entity/Entity;)Z"), method = "interactEntity")
    public boolean interactEntity__common(PlayerEntity instance, Entity entity, Operation<Boolean> original) {
        if (spectatorMode)
            return false;

        return original.call(instance, entity);
    }

    @Inject(at = @At("HEAD"), method = "interactItem", cancellable = true)
    public void interactItem(PlayerEntity player, World world, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (spectatorMode)
            cir.setReturnValue(false);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setSpectatorMode(boolean value) {
        spectatorMode = value;
    }
}
