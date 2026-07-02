package pl.olafcio.renewed.mixin;

import net.minecraft.entity.PathAwareEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends PathAwareEntity {
    public HostileEntityMixin(World world) {
        super(world);
    }

    @Inject(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/mob/HostileEntity;field_3415:Lnet/minecraft/entity/Entity;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            ),
            method = "damage"
    )
    private void damage__setTarget__after(DamageSource source, int damage, CallbackInfoReturnable<Boolean> cir) {
        if (field_3415 instanceof PlayerEntity && ((PlayerEntity) field_3415).abilities.invulnerable)
            field_3415 = null;
    }
}
