package pl.olafcio.renewed.mixin;

import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerPacketListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.olafcio.renewed.mixininterface.ICommandRegistry;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

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

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerPacketListener;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 2), method = "onPlayerConnect")
    public void onPlayerConnect__sendPacket__abilities(ServerPacketListener listener, Packet packet, Connection connection, ServerPlayerEntity player) {
        if (((IServerPlayerEntity) player).isSpectatorMode()) {
            player.noClip = true;

            player.field_2823.sendPacket(new GameStateChangeS2CPacket(
                    6 /* SILENT_GAMEMODE */,
                    3 /* SPECTATOR */
            ));

            player.abilities.invulnerable = true;
            player.abilities.allowFlying = true;
            player.abilities.flying = true;

            player.field_2823.sendPacket(new PlayerAbilitiesS2CPacket(player.abilities));
        }
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ServerPacketListener;sendPacket(Lnet/minecraft/network/Packet;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            method = "teleportToDimension"
    )
    public void teleportToDimension__sendPacket__respawn__after(ServerPlayerEntity player, int dimension, CallbackInfo ci) {
        if (((IServerPlayerEntity) player).isSpectatorMode()) {
            player.noClip = true;

            player.field_2823.sendPacket(new GameStateChangeS2CPacket(
                    6 /* SILENT_GAMEMODE */,
                    3 /* SPECTATOR */
            ));

            player.abilities.invulnerable = true;
            player.abilities.allowFlying = true;
            player.abilities.flying = true;

            player.field_2823.sendPacket(new PlayerAbilitiesS2CPacket(player.abilities));
        }
    }
}
