package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.ControllablePlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.mixinclass.DebugHud;
import pl.olafcio.renewed.mixininterface.IClientPlayerInteractionManager;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ControllablePlayerEntity;isSubmergedIn(Lnet/minecraft/block/material/Material;)Z"), method = "render")
    public boolean render__isSubmergedIn(ControllablePlayerEntity instance, Material material) {
        return instance.isSubmergedIn(Material.WATER) || instance.getAir() < 300;
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;method_956(Ljava/lang/String;III)I",
                    desc = @Desc(value = "method_956", args={int.class, int.class, int.class}, ret = int.class, min = 1, max = 5)
            ),
            method = "render"
    )
    public int render__f3__drawText(TextRenderer instance, String text, int x, int y, int color) {
        return 0;
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    desc = @Desc(value = "drawWithShadow", args={TextRenderer.class, String.class, int.class, int.class, int.class}, ret = int.class, min = 0, max = 7)
            ),
            method = "render"
    )
    public void render__f3__drawWithShadow(InGameHud instance, TextRenderer textRenderer, String string, int x, int y, int color) {}

    @Unique
    private DebugHud debugHud;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(Minecraft mc, CallbackInfo ci) {
        debugHud = new DebugHud(mc);
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            method = "render"
    )
    public void render__f3(float partialTicks, boolean inScreen, int mouseX, int mouseY, CallbackInfo ci) {
        debugHud.render(partialTicks, inScreen, mouseX, mouseY);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isSpectator()Z"), method = "render")
    public boolean render__shouldSkip(ClientPlayerInteractionManager instance, Operation<Boolean> original) {
        return original.call(instance) || ((IClientPlayerInteractionManager) instance).isSpectatorMode();
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 2
            ),
            method = "render"
    )
    public void render__crosshair(InGameHud instance, int a, int b, int c, int d, int e, int f, Operation<Void> original) {
        if (mc.options.perspective == 0)
            original.call(instance, a, b, c, d, e, f);
    }
}
