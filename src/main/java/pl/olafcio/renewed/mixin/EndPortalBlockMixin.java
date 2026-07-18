package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import pl.olafcio.renewed.mixininterface.IServerPlayerEntity;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin extends Block {
    protected EndPortalBlockMixin(int i, Material material) {
        super(i, material);
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, PlayerEntity player, int i, float f, float g, float h) {
        if (!world.isClient && ((IServerPlayerEntity) player).isSpectatorMode()) {
            ServerPlayerEntity serverPlayer = ((ServerPlayerEntity) player);

            serverPlayer.method_3197(1);

            return true;
        }

        return false;
    }
}
