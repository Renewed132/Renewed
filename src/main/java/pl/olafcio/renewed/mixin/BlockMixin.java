package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.features.NewBlock;
import pl.olafcio.renewed.mixininterface.Translatable;

@Mixin(Block.class)
public abstract class BlockMixin
       implements Translatable
{
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void addCustomRegistries(CallbackInfo ci) {
        NewBlock.init();
    }

    @Shadow
    public abstract String getTranslationKey();

    @Override
    public String __getTranslationKey() {
        return getTranslationKey();
    }
}
