package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {
    @Shadow
    private ServerInfo selectedEntry;

    @WrapOperation(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/network/ServerInfo;address:Ljava/lang/String;",
                    opcode = Opcodes.PUTFIELD
            ),
            method = "confirmResult"
    )
    public void confirmResult__serverEdited__openScreen(ServerInfo instance, String value, Operation<Void> original) {
        original.call(instance, value);
        instance.online = false;
        selectedEntry.online = false;
    }
}
