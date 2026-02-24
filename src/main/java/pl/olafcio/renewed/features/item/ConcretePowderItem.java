package pl.olafcio.renewed.features.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import pl.olafcio.renewed.features.NewBlock;
import pl.olafcio.renewed.features.block.ConcretePowderBlock;

public class ConcretePowderItem extends BlockItem {
    public ConcretePowderItem(int i) {
        super(i);
        this.setMaxDamage(0);
        this.setUnbreakable(true);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int method_3343(int i) {
        return NewBlock.CONCRETE_POWDER.method_396(2, ConcretePowderBlock.itemToBlockMETA(i));
    }

    @Override
    public int getMeta(int i) {
        return i;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + DyeItem.field_4196[ConcretePowderBlock.itemToBlockMETA(stack.getData())];
    }
}
