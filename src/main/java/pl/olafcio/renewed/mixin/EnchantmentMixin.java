package pl.olafcio.renewed.mixin;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pl.olafcio.renewed.mixininterface.Translatable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin
       implements Translatable
{
    @Shadow
    public abstract String getTranslationKey();

    @Override
    public String __getTranslationKey() {
        return getTranslationKey();
    }
}
