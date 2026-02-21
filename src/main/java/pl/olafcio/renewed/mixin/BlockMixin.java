package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.features.NewBlock;
import pl.olafcio.renewed.mixin.accessors.IBlock;

@Mixin(Block.class)
public class BlockMixin {
    @Shadow @Final public static Block[] BLOCKS;
    @Shadow @Final public static boolean[] field_495;
    @Shadow @Final public static int[] field_494;
    @Shadow public static boolean[] field_498;

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void inited(CallbackInfo ci) {
        NewBlock.init();

        for (int var0 = 137; var0 < 256; var0++) {
            if (BLOCKS[var0] != null) {
                if (Item.ITEMS[var0] == null) {
                    Item.ITEMS[var0] = new BlockItem(var0 - 256);
                    ((IBlock) BLOCKS[var0]).init();
                }

                boolean var1 = (
                        BLOCKS[var0].getBlockType() == 10 ||
                        BLOCKS[var0] instanceof SlabBlock ||
                        field_495[var0] ||
                        field_494[var0] == 0
                );

                field_498[var0] = var1;
            }
        }
    }
}
