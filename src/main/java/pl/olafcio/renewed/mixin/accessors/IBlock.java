package pl.olafcio.renewed.mixin.accessors;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface IBlock {
    @Invoker("method_472")
    @SuppressWarnings("UnusedReturnValue")
    Block addToRegistry();

    @Invoker("method_477")
    void init();
}
