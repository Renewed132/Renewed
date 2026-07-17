package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BedItem.class)
public class BedItemMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAir(III)Z"), method = "method_3355")
    public boolean use__isAir(World instance, int x, int y, int z) {
        return instance.isAir(x, y, z) || instance.getBlock(x, y, z) == Block.SNOW_LAYER.id;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isTopSolid(III)Z"), method = "method_3355")
    public boolean use__isTopSolid(World instance, int x, int y, int z, ItemStack stack, PlayerEntity player) {
        return player.isSneaking() || instance.isTopSolid(x, y, z);
    }
}
