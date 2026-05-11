package pl.olafcio.renewed.mixin;

import net.minecraft.entity.player.ControllablePlayerEntity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ControllablePlayerEntity.class)
public class ControllablePlayerEntityMixin {
    @ModifyConstant(constant = {
            @Constant(intValue = 4, ordinal = 0)
    }, method = "dropSelectedStack")
    public int dropSelectedStack__actionType(int constant) {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 6 : 4;
    }
}
