package pl.olafcio.renewed.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.ControllablePlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ControllablePlayerEntity;isSubmergedIn(Lnet/minecraft/block/material/Material;)Z"), method = "render")
    public boolean render__isSubmergedIn(ControllablePlayerEntity instance, Material material) {
        return instance.isSubmergedIn(Material.WATER) || instance.getAir() < 300;
    }
}
