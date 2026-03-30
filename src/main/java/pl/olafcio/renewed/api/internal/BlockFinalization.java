package pl.olafcio.renewed.api.internal;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import pl.olafcio.renewed.Renewed;
import pl.olafcio.renewed.api.registry.RegistryAccessor;
import pl.olafcio.renewed.mixin.accessors.IBlock;

import java.util.List;

public enum BlockFinalization {
    ;

    public static void introduce() {
        List<Block> blocks = RegistryAccessor.freeze(Renewed.Blocks);
        for (Block block : blocks)
            ((IBlock) block).addToRegistry();

        for (int var0 = 137; ; var0++) {
            if (Block.BLOCKS[var0] == null) {
                break;
            } else {
                if (Item.ITEMS[var0] == null) {
                    Item.ITEMS[var0] = new BlockItem(var0 - 256);
                    ((IBlock) Block.BLOCKS[var0]).init();
                }

                boolean var1 = (
                        Block.BLOCKS[var0].getBlockType() == 10 ||
                        Block.BLOCKS[var0] instanceof SlabBlock ||
                        Block.field_495[var0] ||
                        Block.field_494[var0] == 0
                );

                Block.field_498[var0] = var1;
            }
        }
    }
}
