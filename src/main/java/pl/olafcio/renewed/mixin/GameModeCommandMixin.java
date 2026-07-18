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
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(GameModeCommand.class)
public class GameModeCommandMixin {
    @Unique
    private int spectator = 0;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/GameModeCommand;method_3540(Lnet/minecraft/command/CommandSource;Ljava/lang/String;)Lnet/minecraft/world/GameMode;"), method = "execute")
    public GameMode execute__parseGamemode(GameModeCommand instance, CommandSource source, String string, Operation<GameMode> original) {
        if (string.equalsIgnoreCase("spectator") || string.equalsIgnoreCase("sp")) {
            this.spectator = 1;
            return GameMode.ADVENTURE;
        }

        return original.call(instance, source, string);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_3170(Lnet/minecraft/world/GameMode;)V"), method = "execute")
    public void execute__setGamemode(PlayerEntity instance, GameMode gameMode, Operation<Void> original) {
        if (this.spectator == 1) {
            this.spectator = 2;
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

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;getGameModeName()Ljava/lang/String;"), method = "execute")
    public String execute__getGamemodeName(GameMode instance, Operation<String> original) {
        if (this.spectator == 2) {
            this.spectator = 0;
            return "spectator";
        }

        return original.call(instance);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/GameModeCommand;getClampedInt(Lnet/minecraft/command/CommandSource;Ljava/lang/String;II)I"), method = "method_3540")
    protected int parseGamemode__getClampedInt(CommandSource commandSource, String string, int min, int max, Operation<Integer> original) {
        int spectator = max + 1;
        int val = original.call(commandSource, string, min, spectator);

        if (val == spectator) {
            this.spectator = 1;
            return GameMode.ADVENTURE.getGameModeId();
        }

        return val;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/GameModeCommand;method_2894([Ljava/lang/String;[Ljava/lang/String;)Ljava/util/List;", ordinal = 0), method = "method_3276")
    public List tabcomplete__gamemodeNames(String[] ref, String[] strings) {
        ArrayList<String> values = Arrays.stream(strings).collect(Collectors.toCollection(ArrayList::new));
        values.add("spectator");
        return GameModeCommand.method_2894(ref, values.toArray(new String[0]));
    }
}
