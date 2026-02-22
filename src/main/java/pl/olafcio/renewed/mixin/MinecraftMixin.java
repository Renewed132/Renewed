package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
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

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public InGameHud inGameHud;
    @Shadow public GameOptions options;
    @Shadow public ClientWorld world;
    @Shadow public ControllablePlayerEntity playerEntity;

    @Unique private static final String ANSI_RESET = "\033[0m";  // Text Reset
    @Unique private static final String ANSI_BLACK = "\033[30m";   // BLACK
    @Unique private static final String ANSI_RED = "\033[31m";     // RED
    @Unique private static final String ANSI_GREEN = "\033[32m";   // GREEN
    @Unique private static final String ANSI_YELLOW = "\033[33m";  // YELLOW
    @Unique private static final String ANSI_BLUE = "\033[34m";    // BLUE
    @Unique private static final String ANSI_PURPLE = "\033[35m";  // PURPLE
    @Unique private static final String ANSI_CYAN = "\033[36m";    // CYAN
    @Unique private static final String ANSI_LIGHT_GRAY = "\033[37m";   // WHITE
    @Unique private static final String ANSI_DARK_GRAY = "\033[90m";   // BLACK

    @Unique
    private String color(String input) {
        return input.replace("{gray}", ANSI_DARK_GRAY)
                    .replace("{red}", ANSI_RED)
                    .replace("{yellow}", ANSI_YELLOW)
                    .replace("{green}", ANSI_GREEN)
                    .replace("{cyan}", ANSI_CYAN)
                    .replace("{reset}", ANSI_RESET)
                    .replace("{white}", ANSI_LIGHT_GRAY);
    }

    @Inject(at = @At("CTOR_HEAD"), method = "<init>")
    public void init(Canvas canvas, MinecraftApplet applet, int width, int height, boolean focused, CallbackInfo ci) {
        Logger root = Logger.getLogger("");

        SimpleDateFormat f_date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat f_time = new SimpleDateFormat("HH:mm:ss");

        for (Handler h : root.getHandlers()) {
            h.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    long ms = record.getMillis();
                    Date date = new Date(ms);

                    return String.format(
                           color("{gray}[{red}%s{gray}] [{yellow}%s{gray}] [{green}%s{gray}]%s{reset} {white}%s%n"),
                            f_date.format(date),
                            f_time.format(date),
                            record.getLevel(),
                            record.getLoggerName() == null
                                    ? ""
                                    : String.format(color(" [{cyan}%s{gray}]"), record.getLoggerName()),
                            record.getMessage()
                    );
                }
            });
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"), method = "tick")
    public boolean tick__isKeyDown__F3(int key, Operation<Boolean> original) {
        boolean res = original.call(key);
        if (key == 61 /* F3 */ && res)
            this.usedHotkey = true;

        return res;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 7), method = "tick")
    public void tick__getEventKey__isD(CallbackInfo ci) {
        // This is injected at the F3+T check
        if (Keyboard.getEventKey() == Keyboard.KEY_D && Keyboard.isKeyDown(Keyboard.KEY_F3 /* 61 */)) {
            this.inGameHud.getChatHud().clear();
            this.usedHotkey = true;
        }
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
