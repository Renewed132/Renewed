package pl.olafcio.renewed.mixin;

import net.minecraft.client.TextureManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_HEIGHT;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Unique private static final float TERRAIN_VALUE = 1F / (float) TERRAIN_SPRITESHEET_HEIGHT;
    @Unique private static final float ITEMS_VALUE = 1F / 256F;

    @Unique private boolean loadedFromTerrainPNG = false;

    @ModifyConstant(
            constant = {
                    @Constant(floatValue = 0.00390625F, ordinal = 1)
            },
            method = "method_1544"
    )
    public float render2dBlock(float constant) {
        return loadedFromTerrainPNG ? TERRAIN_VALUE : ITEMS_VALUE;
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
            method = "method_1545",
            require = 2
    )
    public void usingTerrainTexture(TextRenderer textRenderer, TextureManager textureManager, int itemIdxInRegistry, int i, int j, int x, int y, CallbackInfo ci) {
        loadedFromTerrainPNG = true;
    }

    @Inject(
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                            ordinal = 1
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/TextureManager;getTextureFromPath(Ljava/lang/String;)I",
                            ordinal = 3
                    )
            },
            method = "method_1545",
            require = 2
    )
    public void usingItemsTexture(TextRenderer textRenderer, TextureManager textureManager, int itemIdxInRegistry, int i, int j, int x, int y, CallbackInfo ci) {
        loadedFromTerrainPNG = false;
    }
}
