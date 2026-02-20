package pl.olafcio.renewed.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pl.olafcio.renewed.mixininterface.IInput;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends MobEntity {
    @Shadow public Input input;

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
}
