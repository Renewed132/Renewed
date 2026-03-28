package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.features.NewBlock;
import pl.olafcio.renewed.mixininterface.IMobEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Final public Box boundingBox;
    @Shadow public World world;

    @Shadow public abstract boolean damage(DamageSource source, int damage);

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;y:D", ordinal = 2, opcode = Opcodes.GETFIELD), method = "baseTick")
    public double baseTick__y__below64(Entity instance) {
        double y = instance.y;
        if ((Entity)(Object)this instanceof MobEntity && y >= -64.0)
            ((IMobEntity) this).setDamageTicks(0);

        return y;
    }

    @Unique
    private long lastMagmaDamage = -1;

    @WrapOperation(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;fireTicks:I", ordinal = 2, opcode = Opcodes.GETFIELD), method = "move")
    public int move__fireTicks__notOnFire(Entity instance, Operation<Integer> original) {
        if (containsMagma(this.boundingBox.increment(0.001, 0.001, 0.001))) {
            long now = Minecraft.getTime();
            if (lastMagmaDamage + 800 < now) {
                lastMagmaDamage = now;
                damage(DamageSource.FIRE, 1);
            }

            return 1;
        }

        return original.call(instance);
    }

    @Unique
    public boolean containsMagma(Box box) {
        int x1 = MathHelper.floor(box.minX);
        int x2 = MathHelper.floor(box.maxX + 1.0);
        int y1 = MathHelper.floor(box.minY);
        int y2 = MathHelper.floor(box.maxY + 1.0);
        int z1 = MathHelper.floor(box.minZ);
        int z2 = MathHelper.floor(box.maxZ + 1.0);

        if (this.world.isRegionLoaded(x1, y1, z1, x2, y2, z2)) {
            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    for (int z = z1; z < z2; z++) {
                        int blockID = this.world.getBlock(x, y, z);
                        if (blockID == NewBlock.MAGMA.id) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
