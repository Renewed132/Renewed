package pl.olafcio.renewed.mixinclass;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.Vec3d;

public final class PersistentEntityHitResult extends BlockHitResult {
    public PersistentEntityHitResult() {
        super(0, 0, 0, 0, Vec3d.getVec3dPool().getOrCreate(0, 0, 0));
    }

    public PersistentEntityHitResult update(Entity entity) {
        this.hitResult = HitResultType.ENTITY;
        this.entity = entity;
        this.pos = Vec3d.getVec3dPool().getOrCreate(entity.x, entity.y, entity.z);

        return this;
    }
}
