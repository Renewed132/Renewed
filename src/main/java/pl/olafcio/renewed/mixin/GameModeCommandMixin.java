package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

@Mixin(GameModeCommand.class)
public class GameModeCommandMixin {
    @Unique
    private boolean spectator = false;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/GameModeCommand;method_3540(Lnet/minecraft/command/CommandSource;Ljava/lang/String;)Lnet/minecraft/world/GameMode;"), method = "execute")
    public GameMode execute__parseGamemode(GameModeCommand instance, CommandSource source, String string, Operation<GameMode> original) {
        if (string.equalsIgnoreCase("spectator") || string.equalsIgnoreCase("sp")) {
            this.spectator = true;
            return GameMode.ADVENTURE;
        }

        return original.call(instance, source, string);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_3170(Lnet/minecraft/world/GameMode;)V"), method = "execute")
    public void execute__setGamemode(PlayerEntity instance, GameMode gameMode, Operation<Void> original) {
        if (this.spectator) {
            this.spectator = false;
            instance.noClip = true;

            ServerPlayerEntity player = (ServerPlayerEntity) instance;

            player.field_2823.sendPacket(new GameStateChangeS2CPacket(
                    3 /* GAMEMODE */,
                    3 /* SPECTATOR */
            ));

            ((IServerPlayerEntity) instance).setSpectatorMode(true);

            player.interactionManager.setGameMode(gameMode);

            player.abilities.invulnerable = true;
            player.abilities.allowFlying = true;
            player.abilities.flying = true;

            player.field_2823.sendPacket(new PlayerAbilitiesS2CPacket(player.abilities));
        } else {
            instance.noClip = false;

            original.call(instance, gameMode);

            ((IServerPlayerEntity) instance).setSpectatorMode(false);
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/GameModeCommand;getClampedInt(Lnet/minecraft/command/CommandSource;Ljava/lang/String;II)I"), method = "method_3540")
    protected int parseGamemode__getClampedInt(CommandSource commandSource, String string, int min, int max, Operation<Integer> original) {
        int spectator = max + 1;
        int val = original.call(commandSource, string, min, spectator);

        if (val == spectator) {
            this.spectator = true;
            return GameMode.ADVENTURE.getGameModeId();
        }

        return val;
    }
}
