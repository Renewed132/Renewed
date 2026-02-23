package pl.olafcio.renewed.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import pl.olafcio.renewed.mixininterface.IParticle;

import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_ROWS;

@Mixin(Particle.class)
public class ParticleMixin
       implements IParticle
{
    @Unique
    private boolean loadedFromTerrain;

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 16.0F, ordinal = 1)
            },
            method = "method_1283"
    )
    public float vRows(float constant) {
        return loadedFromTerrain ? TERRAIN_SPRITESHEET_ROWS : 16.0F;
    }

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 0.0624375F, ordinal = 1)
            },
            method = "method_1283"
    )
    public float v1DivByRows(float constant) {
        return loadedFromTerrain ? 1/TERRAIN_SPRITESHEET_ROWS : 1/16.0F;
    }

    @Override
    @SuppressWarnings("all")
    public void setLoadedFromTerrain(boolean value) {
        loadedFromTerrain = value;
    }
}
