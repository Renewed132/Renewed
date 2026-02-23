package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.mixininterface.IParticle;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Unique
    private boolean loadedFromTerrainPNG = false;

    @SuppressWarnings("all")
    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/Particle;method_1283(Lnet/minecraft/client/render/Tessellator;FFFFFF)V"
            ),
            method = "renderParticles"
    )
    public void addParticleVertex(Particle instance, Tessellator f, float g, float h, float i, float j, float k, float v, Operation<Void> original) {
        ((IParticle) instance).setLoadedFromTerrain(loadedFromTerrainPNG);
        original.call(instance, f, g, h, i, j, k, v);
    }

    @Inject(
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                            ordinal = 0
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                            ordinal = 2
                    )
            },
            method = "renderParticles"
    )
    public void renderParticles__NonTerrain(Entity entity, float tickDelta, CallbackInfo ci) {
        loadedFromTerrainPNG = false;
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                    ordinal = 1
            ),
            method = "renderParticles"
    )
    public void renderParticles__Terrain(Entity entity, float tickDelta, CallbackInfo ci) {
        loadedFromTerrainPNG = true;
    }
}
