package pl.olafcio.renewed.mixin;

import net.minecraft.client.TextureManager;
import net.minecraft.client.texture.ITexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.renewed.Renewed;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Unique
    private static final HashMap<String, InputStream> inputStreams
                   = new HashMap<>();

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/ITexturePack;openStream(Ljava/lang/String;)Ljava/io/InputStream;"), method = "getTextureFromPath")
    public InputStream openStream(ITexturePack texturePack, String path) {
        if (inputStreams.containsKey(path)) {
            try {
                InputStream stream = inputStreams.get(path);
                stream.reset();
                return stream;
            } catch (IOException e) {
                throw new RuntimeException("Failed to reset() a cached mod texture stream", e);
            }
        } else {
            InputStream stream = Renewed.class.getResourceAsStream("/overrides" + path);
            if (stream != null) {
                inputStreams.put(path, stream);
                return stream;
            }

            return texturePack.openStream(path);
        }
    }
}
