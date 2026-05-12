package pl.olafcio.renewed.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.mixininterface.IWindow;

@Mixin(Window.class)
public class WindowMixin
       implements IWindow
{
    @Unique private int unscaledWidth;
    @Unique private int unscaledHeight;

    @Inject(at = @At("CTOR_HEAD"), method = "<init>")
    private void init(GameOptions options, int width, int height, CallbackInfo ci) {
        this.unscaledWidth = width;
        this.unscaledHeight = height;
    }

    @Override public int unscaledWidth() {
        return unscaledWidth;
    }
    @Override public int unscaledHeight() {
        return unscaledHeight;
    }

    @Shadow private int width;
    @Shadow private int height;

    @Shadow private int scaleFactor;

    @Shadow private double scaledWidth;
    @Shadow private double scaledHeight;

    @Override
    public void update(GameOptions options, int width, int height) {
        this.unscaledWidth = width;
        this.unscaledHeight = height;

        this.width = width;
        this.height = height;

        this.scaleFactor = 1;

        int n = options.guiScale;
        if (n == 0) {
            n = 1000;
        }

        while (this.scaleFactor < n && this.width / (this.scaleFactor + 1) >= 320 && this.height / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        this.scaledWidth = (double)this.width / (double)this.scaleFactor;
        this.scaledHeight = (double)this.height / (double)this.scaleFactor;

        this.width = MathHelper.ceil(this.scaledWidth);
        this.height = MathHelper.ceil(this.scaledHeight);
    }
}
