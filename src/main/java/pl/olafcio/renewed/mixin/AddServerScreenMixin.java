package pl.olafcio.renewed.mixin;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public class AddServerScreenMixin {
    @Shadow
    private ServerInfo server;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;confirmResult(ZI)V", ordinal = 1), method = "buttonClicked")
    protected void buttonClicked__confirmResult__changed(ButtonWidget button, CallbackInfo ci) {
        this.server.online = false;
        this.server.ping = -2L;
    }
}
