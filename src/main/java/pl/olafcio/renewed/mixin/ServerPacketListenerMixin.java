package pl.olafcio.renewed.mixin;

import net.minecraft.server.ServerPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPacketListener.class)
public class ServerPacketListenerMixin {
    @ModifyConstant(constant = {
            @Constant(intValue = 100)
    }, method = "onChatMessage")
    public int onChatMessage(int constant) {
        return 255;
    }
}
