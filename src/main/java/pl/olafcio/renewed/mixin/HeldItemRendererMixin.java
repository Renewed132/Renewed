package pl.olafcio.renewed.mixin;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_HEIGHT;
import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_ROWS;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Unique private static final float TERRAIN_VALUE = 1F / TERRAIN_SPRITESHEET_ROWS;
    @Unique private static final float ITEMS_VALUE = 1F / 16F;

    @Unique private boolean loadedFromTerrainPNG = false;

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 0.0625F, ordinal = 2)
            },
            method = "method_1356",
            require = 1
    )
    public float changeYMagic(float constant) {
        return loadedFromTerrainPNG ? TERRAIN_VALUE : ITEMS_VALUE;
    }

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 16F, ordinal = 2),
                    @Constant(floatValue = 16F, ordinal = 3)
            },
            method = "method_1356",
            require = 2
    )
    public float changeYRows(float constant) {
        return loadedFromTerrainPNG ? TERRAIN_SPRITESHEET_ROWS : 16F;
    }

    @ModifyConstant(
            constant = {
                    @Constant(intValue = 16, ordinal = 2),
                    @Constant(intValue = 16, ordinal = 3)
            },
            method = "method_1356",
            require = 2
    )
    public int changeYRows_(int constant) {
        return loadedFromTerrainPNG ? (int)TERRAIN_SPRITESHEET_ROWS : 16;
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
                            ordinal = 1
                    )
            },
            method = "method_1357",
            require = 2
    )
    public void usingTerrainTexture(MobEntity itemStack, ItemStack i, int par3, CallbackInfo ci) {
        loadedFromTerrainPNG = true;
    }

    @Inject(
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                            ordinal = 2
                    )
            },
            method = "method_1357",
            require = 1
    )
    public void usingItemsTexture(MobEntity itemStack, ItemStack i, int par3, CallbackInfo ci) {
        loadedFromTerrainPNG = false;
    }

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 256.0F, ordinal = 2),
                    @Constant(floatValue = 256.0F, ordinal = 3)
            },
            method = "method_1357"
    )
    public float change256vertical(float constant) {
        return loadedFromTerrainPNG ? (float)TERRAIN_SPRITESHEET_HEIGHT : 256.0F;
    }
}
