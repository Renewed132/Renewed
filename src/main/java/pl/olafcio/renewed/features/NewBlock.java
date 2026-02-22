package pl.olafcio.renewed.features;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import pl.olafcio.renewed.features.block.ConcreteBlock;
import pl.olafcio.renewed.features.block.TerracottaBlock;
import pl.olafcio.renewed.features.item.ConcreteItem;
import pl.olafcio.renewed.features.item.TerracottaItem;
import pl.olafcio.renewed.mixin.accessors.IBlock;

public class NewBlock {
    public static Block CONCRETE
           = new ConcreteBlock().setTranslationKey("concrete");

    public static Block TERRACOTTA
            = new TerracottaBlock().setTranslationKey("terracotta");

    public static void init() {
        ((IBlock) CONCRETE).addToRegistry();
        ((IBlock) TERRACOTTA).addToRegistry();

        Item.ITEMS[CONCRETE.id]   = new ConcreteItem(CONCRETE.id - 256).setName("concrete");
        Item.ITEMS[TERRACOTTA.id] = new TerracottaItem(TERRACOTTA.id - 256).setName("terracotta");
    }
}
