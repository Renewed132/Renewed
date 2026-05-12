package pl.olafcio.renewed.mixin.accessors;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextRenderer.class)
public interface ITextRenderer {
    @Invoker("method_964")
    void renderWithoutShadow(String string, int i, int j, int k);
}
