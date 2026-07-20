package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @WrapMethod(method = "method_2170")
    public boolean useBlock(PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int i, float f, float g, float h, Operation<Boolean> original) {
        int block = world.getBlock(x, y, z);
        if (block == Block.SNOW_LAYER.id && !player.isSneaking())
            y -= 1;

        return original.call(player, world, stack, x, y, z, i, f, g, h);
    }
}
