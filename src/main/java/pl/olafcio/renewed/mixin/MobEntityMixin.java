package pl.olafcio.renewed.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pl.olafcio.renewed.mixininterface.IMobEntity;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin
       extends Entity
       implements IMobEntity
{
    public MobEntityMixin(World world) {
        super(world);
    }

    // VOID DAMAGE //

    @Unique
    private int damageTicks = 0;

    @Inject(at = @At("HEAD"), method = "destroy", cancellable = true)
    public void destroy(CallbackInfo ci) {
        if (damageTicks++ == 0) {
            damageTicks = getVoidDamageDelay();
            ci.cancel();
        }
    }

    @Override
    @SuppressWarnings("all")
    public void setDamageTicks(int damageTicks) {
        this.damageTicks = damageTicks;
    }

    @Override
    @SuppressWarnings("all")
    public int getVoidDamageDelay() {
        return -80;
    }

    // AIR REGENERATION //

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;setAir(I)V", ordinal = 2), method = "baseTick")
    public void baseTick__setAir__outOfWater(Args args) {
        int value = this.getAir();
        if (value < 300)
            args.set(0, Math.min(300, value + 4));
    }
}
