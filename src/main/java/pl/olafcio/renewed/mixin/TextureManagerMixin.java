package pl.olafcio.renewed.mixin;

import net.minecraft.client.TextureManager;
import net.minecraft.client.texture.ITexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.Renewed;

import java.io.InputStream;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/ITexturePack;openStream(Ljava/lang/String;)Ljava/io/InputStream;"), method = "getTextureFromPath")
    public InputStream openStream(ITexturePack texturePack, String path) {
        InputStream stream = Renewed.class.getResourceAsStream("/overrides" + path);
        if (stream != null)
            return stream;

        return texturePack.openStream(path);
    }
}
