package pl.olafcio.renewed.mixin;

import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.Renewed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Mixin(Language.class)
public class LanguageMixin {
    @Inject(at = @At("TAIL"), method = "method_633")
    private void loadLanguage(Properties properties, String language, CallbackInfo ci) {
        try {
            InputStream resource = Renewed.class.getResourceAsStream("/overrides/lang/" + language + ".lang");
            if (resource == null)
                return;

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
            throw new RuntimeException(e);
        }
    }
}
