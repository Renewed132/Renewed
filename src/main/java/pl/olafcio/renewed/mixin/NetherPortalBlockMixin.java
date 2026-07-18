package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin extends Block {
    protected NetherPortalBlockMixin(int i, Material material) {
        super(i, material);
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, PlayerEntity player, int i, float f, float g, float h) {
        if (!world.isClient && ((IServerPlayerEntity) player).isSpectatorMode()) {
            ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) player);

            int newDimension = serverPlayer.dimension == -1 ? 0 : -1;
            serverPlayer.server.getPlayerManager().teleportToDimension(serverPlayer, newDimension);

            return true;
        }

        return false;
    }
}
