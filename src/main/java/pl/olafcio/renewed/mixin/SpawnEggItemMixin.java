package pl.olafcio.renewed.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.CommonI18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin extends Item {
    protected SpawnEggItemMixin(int i) {
        super(i);
    }

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void getDisplayName(ItemStack stack, CallbackInfoReturnable<String> cir) {
        String var2 = CommonI18n.translate(this.getTranslationKey() + ".name").trim();
        String var3 = EntityType.getEntityName(stack.getData());

        if (var3 != null)
            var2 = CommonI18n.translate("entity." + var3 + ".name") + " " + var2;

        cir.setReturnValue(var2);
    }
}
