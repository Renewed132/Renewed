package pl.olafcio.renewed.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.TextureManager;
import net.minecraft.client.texture.ITexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

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
            Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
            for (ModContainer container : mods) {
                Optional<Path> opt = container.findPath("overrides" + path);
                if (opt.isPresent()) {
                    Path obj = opt.get();
                    if (Files.exists(obj)) {
                        try {
                            InputStream stream = Files.newInputStream(obj, StandardOpenOption.READ);
                            inputStreams.put(path, stream);

                            return stream;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to use override '" + obj + "'", e);
                        }
                    }
                }
            }

            return texturePack.openStream(path);
        }
    }
}
