package pl.olafcio.renewed.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity {
    public MobEntityMixin(World world) {
        super(world);
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;setAir(I)V", ordinal = 2), method = "baseTick")
    public void baseTick__setAir__outOfWater(Args args) {
        int value = this.getAir();
        if (value < 300)
            args.set(0, Math.min(300, value + 4));
    }
}
