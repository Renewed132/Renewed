package pl.olafcio.renewed.features.frostburn.item;

import net.minecraft.item.BlockItem;

public class MagmaItem extends BlockItem {
    public MagmaItem(int i) {
        super(i);
        this.setMaxDamage(0);
        this.setUnbreakable(true);
    }
}
