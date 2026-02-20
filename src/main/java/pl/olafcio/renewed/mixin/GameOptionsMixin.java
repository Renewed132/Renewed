package pl.olafcio.renewed.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.mixininterface.IGameOptions;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements IGameOptions {
    @Shadow public float fov;
    @Shadow public float gamma;

    @Unique
    public KeyBinding sprintKey = new KeyBinding("key.sprint", Keyboard.KEY_LCONTROL);

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fov:F", opcode = Opcodes.PUTFIELD), method = "<init>*")
    public void init__fov(GameOptions instance, float value) {
        fov = 77/180f;
    }

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;gamma:F", opcode = Opcodes.PUTFIELD), method = "<init>*")
    public void init__gamma(GameOptions instance, float value) {
        gamma = 50/100f;
    }

    @Override
    @SuppressWarnings("all")
    public KeyBinding sprintKey() {
        return sprintKey;
    }
}
