package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.CullingCameraView;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.mixinclass.PersistentEntityHitResult;
import pl.olafcio.renewed.mixininterface.IClientPlayerInteractionManager;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique
    private final PersistentEntityHitResult entityHR
            = new PersistentEntityHitResult();

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/hit/BlockHitResult;"), method = "updateTargetedEntity")
    public BlockHitResult updateTargetedEntity__newBlockHitResult(Entity entity) {
        return entityHR.update(entity);
    }

    @Unique
    private final CullingCameraView cullingCameraView
            = new CullingCameraView();

    @Redirect(at = @At(value = "NEW", target = "()Lnet/minecraft/client/render/CullingCameraView;"), method = "renderWorld")
    public CullingCameraView renderWorld__newCullingCameraView() {
        return cullingCameraView;
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isSpectator()Z"
            ),
            method = "renderHand"
    )
    private boolean renderHand__isSpectatorMode(ClientPlayerInteractionManager instance, Operation<Boolean> original) {
        return original.call(instance) || ((IClientPlayerInteractionManager) instance).isSpectatorMode();
    }
}
