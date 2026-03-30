package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(at = @At("HEAD"), name = "i", index = 1, ordinal = 0, method = "<init>(ILnet/minecraft/block/material/Material;)V", argsOnly = true)
    private static int init__blockID(int i) {
        if (i == -1) {
            for (int id = 1; id < BLOCKS.length; id++)
                if (BLOCKS[id] == null)
                    return id;
        }

        return i;
    }

    @Shadow
    @Final
    public static Block[] BLOCKS;

    @Shadow
    public abstract String getTranslationKey();

    @Override
    public String __getTranslationKey() {
        return getTranslationKey();
    }
}
