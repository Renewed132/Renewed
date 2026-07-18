package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientLoopbackPlayNetworkHandler;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.renewed.mixininterface.IClientPlayerInteractionManager;

@Mixin(ClientLoopbackPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow private Minecraft mc;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;setGameModeWithId(I)Lnet/minecraft/world/GameMode;"), method = "onGameStateChange")
    public GameMode onGameStateChange__setGamemodeWithId(int gamemode, Operation<GameMode> original) {
        if (gamemode == 3) {
            gamemode = 2;
            this.mc.playerEntity.noClip = true;
            ((IClientPlayerInteractionManager) this.mc.interactionManager).setSpectatorMode(true);
        } else {
            this.mc.playerEntity.noClip = false;
            ((IClientPlayerInteractionManager) this.mc.interactionManager).setSpectatorMode(false);
        }

        return original.call(gamemode);
    }
}
