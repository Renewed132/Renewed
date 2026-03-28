package pl.olafcio.renewed.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pl.olafcio.renewed.mixininterface.Translatable;

@Mixin(Item.class)
public abstract class ItemMixin
       implements Translatable
{
    @Shadow
    public abstract String getTranslationKey();

    @Override
    public String __getTranslationKey() {
        return getTranslationKey();
    }
}
