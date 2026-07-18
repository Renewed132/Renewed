package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.ServerPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPacketListener.class)
public class ServerPacketListenerMixin {
    @Shadow
    private ServerPlayerEntity player;

    @ModifyConstant(constant = {
            @Constant(intValue = 100)
    }, method = "onChatMessage")
    public int onChatMessage(int constant) {
        return 255;
    }

    @Inject(at = @At("HEAD"), method = "onPlayerAction", cancellable = true)
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (packet.action == 6) {
            ci.cancel();

            int slot = player.inventory.selectedSlot;
            ItemStack stack = player.inventory.getInvStack(slot);

            if (stack == null || stack.count < 1)
                return;

            player.dropStack(player.inventory.takeInvStack(slot, stack.count), false);
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;method_2641()Z"), method = "onPlayerMove")
    public boolean onPlayerMove__noClip(ServerPlayerEntity instance, Operation<Boolean> original) {
        return original.call(instance) || instance.noClip;
    }
}
