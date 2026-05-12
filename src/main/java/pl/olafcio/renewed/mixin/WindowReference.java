package pl.olafcio.renewed.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AchievementNotification;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LoadingScreenRenderer;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.mixininterface.IMinecraft;
import pl.olafcio.renewed.mixininterface.IWindow;

@Mixin({ InGameHud.class, Minecraft.class, AchievementNotification.class, ChatHud.class, VideoOptionsScreen.class, EnchantingScreen.class, GameRenderer.class, LoadingScreenRenderer.class })
public class WindowReference {
    @Redirect(
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/option/GameOptions;II)Lnet/minecraft/client/util/Window;"
            ),
            method = {
                    // InGameHud
                    "renderBossBar",
                    // Minecraft
                    "method_2915", "openScreen", "method_2923",
                    // AchievementNotification
                    "render",
                    // ChatHud
                    "method_897",
                    // VideoOptionsScreen
                    "buttonClicked",
                    // EnchantingScreen
                    "drawBackground",
                    // GameRenderer
                    "renderGui", "setupHudMatrixMode",
                    // LoadingScreenRenderer
                    "method_884", "setProgressPercentage"
            },
            require = 0
    )
    public Window newWindow(GameOptions options, int width, int height) {
        return ((IMinecraft) Minecraft.getMinecraft()).window();
    }

    @SuppressWarnings("all")
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;<init>(Lnet/minecraft/client/option/GameOptions;II)V"), method = "buttonClicked", target = {
            @Desc(value = "buttonClicked", owner = VideoOptionsScreen.class, args = { ButtonWidget.class })
    })
    public void changedGuiScale(ButtonWidget button, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();

        ((IWindow) ((IMinecraft) mc).window())
                .update(mc.options, mc.width, mc.height);
    }
}
