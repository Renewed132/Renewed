package pl.olafcio.renewed.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.mixininterface.IMobEntity;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;y:D", ordinal = 2, opcode = Opcodes.GETFIELD), method = "baseTick")
    public double baseTick__y__below64(Entity instance) {
        double y = instance.y;
        if ((Entity)(Object)this instanceof MobEntity && y >= -64.0)
            ((IMobEntity) this).setDamageTicks(0);

        return y;
    }
}
