package pl.olafcio.renewed.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pl.olafcio.renewed.mixininterface.IGameOptions;

@Mixin(GameOptions.class)
@SuppressWarnings("all")
public class GameOptionsMixin implements IGameOptions {
    @Unique
    public KeyBinding sprintKey = new KeyBinding("key.sprint", Keyboard.KEY_LCONTROL);

    @Override
    public KeyBinding sprintKey() {
        return sprintKey;
    }
}
