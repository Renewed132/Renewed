package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
       extends PlayerEntity
       implements IServerPlayerEntity
{
    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Unique
    private boolean spectator = false;

    @Override
    public boolean isSpectatorMode() {
        return spectator;
    }

    @Override
    public void setSpectatorMode(boolean value) {
        spectator = value;
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;setGameModeWithId(I)Lnet/minecraft/world/GameMode;"), method = "readCustomDataFromNbt")
    public GameMode readCustomDataFromNbt__getGameMode(int gamemode, Operation<GameMode> original) {
        if (gamemode == 3) {
            gamemode = 2;

            this.spectator = true;
        }

        return original.call(gamemode);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;setGameMode(Lnet/minecraft/world/GameMode;)V"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt__setGameMode(ServerPlayerInteractionManager instance, GameMode gameMode, Operation<Void> original) {
        if (this.spectator) {
            this.setSpectatorMode(true);
        } else {
            original.call(instance, gameMode);
        }
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putInt(Ljava/lang/String;I)V", ordinal = 0), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt__saveGameMode(Args args) {
        if (this.spectator)
            args.set(1, 3);
    }

    @Override
    @SuppressWarnings("all")
    public PlayerInventory getInventory() {
        return inventory;
    }
}
