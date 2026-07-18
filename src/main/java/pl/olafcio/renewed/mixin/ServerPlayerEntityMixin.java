package pl.olafcio.renewed.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    @Override
    @SuppressWarnings("all")
    public PlayerInventory getInventory() {
        return inventory;
    }
}
