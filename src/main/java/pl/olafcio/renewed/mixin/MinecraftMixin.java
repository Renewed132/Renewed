package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ControllablePlayerEntity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public InGameHud inGameHud;
    @Shadow public GameOptions options;
    @Shadow public ClientWorld world;
    @Shadow public ControllablePlayerEntity playerEntity;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"), method = "tick")
    public boolean tick__isKeyDown__F3(int key, Operation<Boolean> original) {
        boolean res = original.call(key);
        if (key == 61 /* F3 */ && res)
            this.usedHotkey = true;

        return res;
    }

    @Unique
    private boolean usedHotkey = false;

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 9), method = "tick")
    public int tick__getEventKey__isF3() {
        int key = Keyboard.getEventKey();
        if (key == Keyboard.KEY_F3 /* 61 */) {
            usedHotkey = false;

            boolean shift = !Screen.hasShiftDown();
            new Thread(() -> {
                try {
                    for (int i = 0; i < 6; i++) {
                        Thread.sleep(55);

                        if (!Keyboard.isKeyDown(Keyboard.KEY_F3) && condEnableF3(shift))
                            return;
                    }

                    Thread.sleep(180);
                    condEnableF3(shift);
                } catch (InterruptedException ignored) {}
            }).start();
        }

        return -1;
    }

    @Unique
    private boolean condEnableF3(boolean shift) {
        if (!usedHotkey && world != null && playerEntity != null) {
            this.options.debugEnabled = !this.options.debugEnabled;
            this.options.debugProfilerEnabled = shift;

            return true;
        } else {
            return false;
        }
    }
}
