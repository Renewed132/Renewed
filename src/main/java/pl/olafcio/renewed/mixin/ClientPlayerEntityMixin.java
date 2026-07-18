package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.Input;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.renewed.mixininterface.IClientPlayerInteractionManager;
import pl.olafcio.renewed.mixininterface.IInput;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    @Shadow public Input input;
    @Shadow protected Minecraft field_1759;

    public ClientPlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public boolean isSprinting() {
        return ((IInput) input).isSprinting();
    }

    @Override
    protected float getSpeed() {
        float value = super.getSpeed();
        if (isSprinting())
            value *= 1.3F;

        return value;
    }

    @Override
    public boolean isInsideWall() {
        return super.isInsideWall() && !((IClientPlayerInteractionManager) this.field_1759.interactionManager).isSpectatorMode();
    }

    @WrapOperation(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerAbilities;allowFlying:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            ),
            method = "method_2651"
    )
    private boolean m2651__allowFlying__1(PlayerAbilities instance, Operation<Boolean> original) {
        return original.call(instance) && !((IClientPlayerInteractionManager) this.field_1759.interactionManager).isSpectatorMode();
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ClientPlayerEntity;pushOutOfBlocks(DDD)Z"
            ),
            method = "method_2651"
    )
    private boolean m2561__pushOutOfBlocks(ClientPlayerEntity instance, double x, double y, double z, Operation<Boolean> original) {
        if (((IClientPlayerInteractionManager) this.field_1759.interactionManager).isSpectatorMode())
            return false;

        return original.call(instance, x, y, z);
    }
}
