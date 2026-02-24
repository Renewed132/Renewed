package pl.olafcio.renewed.mixin;

import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pl.olafcio.renewed.mixininterface.IMaterial;

@Mixin(Material.class)
public abstract class MaterialMixin
       implements IMaterial
{
    @Shadow public abstract boolean isFluid();
}
