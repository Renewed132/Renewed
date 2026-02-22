package pl.olafcio.renewed.mixin;

import net.minecraft.client.texture.ZipTexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(ZipTexturePack.class)
public class ZipTexturePackMixin {
    @Shadow private ZipFile zip;

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/zip/ZipFile;getEntry(Ljava/lang/String;)Ljava/util/zip/ZipEntry;"), method = "openStream", cancellable = true)
    public void openStream__getEntry(String path, CallbackInfoReturnable<InputStream> cir) {
        try {
            ZipEntry var2 = this.zip.getEntry("assets/minecraft/" + path.substring(1));
            if (var2 != null)
                cir.setReturnValue(this.zip.getInputStream(var2));
        } catch (Exception ignored) {}
    }
}
