package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.olafcio.renewed.mixininterface.IMaterial;

@Mixin(SandBlock.class)
public class SandBlockMixin {
    @Inject(
            at = @At(value = "HEAD"),
            method = "method_380",
            cancellable = true
    )
    private static void canBePutAt__returnIsFluid(World world, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int id = world.getBlock(i, j, k);
        if (id == 0) {
            cir.setReturnValue(true);
        } else if (id == Block.FIRE.id) {
            cir.setReturnValue(true);
        } else {
            Material material = Block.BLOCKS[id].material;
            cir.setReturnValue(
                ((IMaterial) material)
                    .canBeNaturallyReplacedWithBlock()
            );
        }
    }
}
