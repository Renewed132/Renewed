package pl.olafcio.renewed.mixin;

import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.olafcio.renewed.mixininterface.ICommandRegistry;

import java.util.List;
import java.util.Random;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private MinecraftServer server;
    @Shadow @Final public List<?> players;

    @Inject(at = @At("HEAD"), method = "getPlayer", cancellable = true)
    public void getPlayer(String username, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        switch (username) {
            case "@s": {
                CommandSource currentSource = ((ICommandRegistry) server.getCommandManager()).currentSource();
                if (!(currentSource instanceof ServerPlayerEntity))
                    throw new SyntaxException("The selector '@s' can only be used by a player");

                cir.setReturnValue((ServerPlayerEntity) currentSource);
                break;
            }

            case "@r": {
                int index = new Random().nextInt(this.players.size());
                Object player = this.players.get(index);

                cir.setReturnValue((ServerPlayerEntity) player);
                break;
            }

            case "@p": {
                CommandSource currentSource = ((ICommandRegistry) server.getCommandManager()).currentSource();
                if (!(currentSource instanceof ServerPlayerEntity))
                    throw new SyntaxException("The selector '@p' can only be used by a player");

                // TODO: Make this actually do what it's supposed to, when I add command blocks
                cir.setReturnValue((ServerPlayerEntity) currentSource);
                break;
            }
        }
    }
}
