package pl.olafcio.renewed.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

@Mixin(Language.class)
public class LanguageMixin {
    @Inject(at = @At("TAIL"), method = "method_633")
    private void loadLanguage(Properties properties, String language, CallbackInfo ci) {
        String path = "/overrides/lang/" + language + ".lang";
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();

        for (ModContainer container : mods) {
            Optional<Path> opt = container.findPath(path);
            if (opt.isPresent()) {
                Path obj = opt.get();
                if (Files.exists(obj)) {
                    try {
                        InputStream stream = Files.newInputStream(obj, StandardOpenOption.READ);
                        override(properties, language, stream);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to use language override '" + obj + "'", e);
                    }
                }
            }
        }
    }

    @Unique
    private void override(Properties properties, String language, InputStream resource) {
        try {
            BufferedReader var3 = new BufferedReader(
                                  new InputStreamReader(
                                        resource,
                                        StandardCharsets.UTF_8
                                  ));

            for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
                var4 = var4.trim();
                if (!var4.startsWith("#")) {
                    String[] var5 = var4.split("=");
                    if (var5 != null && var5.length == 2) {
                        properties.setProperty(var5[0], var5[1]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read '" + language + "' language overrides", e);
        }
    }
}
