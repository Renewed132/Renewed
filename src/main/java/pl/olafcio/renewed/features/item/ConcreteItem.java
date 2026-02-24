package pl.olafcio.renewed.features.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import pl.olafcio.renewed.features.NewBlock;
import pl.olafcio.renewed.features.block.ConcreteBlock;

public class ConcreteItem extends BlockItem {
    public ConcreteItem(int i) {
        super(i);
        this.setMaxDamage(0);
        this.setUnbreakable(true);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int method_3343(int i) {
        return NewBlock.CONCRETE.method_396(2, ConcreteBlock.itemToBlockMETA(i));
    }

    @Override
    public int getMeta(int i) {
        return i;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + DyeItem.field_4196[ConcreteBlock.itemToBlockMETA(stack.getData())];
    }
}
