package pl.olafcio.renewed.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
       extends PlayerEntity
       implements IServerPlayerEntity
{
    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    @SuppressWarnings("all")
    public PlayerInventory getInventory() {
        return inventory;
    }
}
