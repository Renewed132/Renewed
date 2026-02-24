package pl.olafcio.renewed.features;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import pl.olafcio.renewed.features.block.ConcreteBlock;
import pl.olafcio.renewed.features.block.ConcretePowderBlock;
import pl.olafcio.renewed.features.block.TerracottaBlock;
import pl.olafcio.renewed.features.item.ConcreteItem;
import pl.olafcio.renewed.features.item.ConcretePowderItem;
import pl.olafcio.renewed.features.item.TerracottaItem;
import pl.olafcio.renewed.mixin.accessors.IBlock;

public class NewBlock {
    public static Block CONCRETE
           = new ConcreteBlock().setTranslationKey("concrete");

    public static Block CONCRETE_POWDER
           = new ConcretePowderBlock().setTranslationKey("concrete_powder");

    public static Block TERRACOTTA
           = new TerracottaBlock().setTranslationKey("terracotta");

    public static void init() {
        ((IBlock) CONCRETE).addToRegistry();
        ((IBlock) CONCRETE_POWDER).addToRegistry();
        ((IBlock) TERRACOTTA).addToRegistry();

        Item.ITEMS[CONCRETE.id]        = new ConcreteItem(CONCRETE.id - 256).setName("concrete");
        Item.ITEMS[CONCRETE_POWDER.id] = new ConcretePowderItem(CONCRETE_POWDER.id - 256).setName("concrete_powder");
        Item.ITEMS[TERRACOTTA.id]      = new TerracottaItem(TERRACOTTA.id - 256).setName("terracotta");
    }
}
