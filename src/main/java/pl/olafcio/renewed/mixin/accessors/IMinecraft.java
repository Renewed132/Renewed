package pl.olafcio.renewed.mixin.accessors;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMinecraft {
    @Accessor("fpsCounter")
    int fpsCounter();
}
