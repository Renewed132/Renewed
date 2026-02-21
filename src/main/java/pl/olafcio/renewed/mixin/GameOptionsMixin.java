package pl.olafcio.renewed.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.class_651;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.common.base.Strings;
import pl.olafcio.renewed.mixininterface.IGameOptions;
import pl.olafcio.renewed.options.Field;
import pl.olafcio.renewed.options.FieldMap;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin implements IGameOptions {
    @Shadow public float fov;
    @Shadow public float gamma;

    @Shadow public float musicVolume;
    @Shadow public float soundVolume;
    @Shadow public boolean invertYMouse;
    @Shadow public float sensitivity;
    @Shadow private File optionsFile;
    @Shadow public int renderDistance;
    @Shadow public int guiScale;
    @Shadow public int particle;
    @Shadow public boolean bobView;
    @Shadow public boolean anaglyph3d;
    @Shadow public boolean advancedOpengl;
    @Shadow public int maxFramerate;
    @Shadow public int difficultyLevel;
    @Shadow public boolean fancyGraphics;
    @Shadow public boolean ambientOcculsion;
    @Shadow public String currentTexturePackName;
    @Shadow public boolean renderClouds;
    @Shadow public String lastServer;
    @Shadow public String language;
    @Shadow public int chatVisibility;
    @Shadow public boolean chatColor;
    @Shadow public boolean chatLink;
    @Shadow public boolean chatLinkPrompt;
    @Shadow public float chatOpacity;
    @Shadow public boolean useServerTextures;
    @Shadow public boolean snopperEnabled;
    @Shadow public boolean fullscreen;
    @Shadow public boolean vsync;
    @Shadow public boolean hideServerAddress;
    @Shadow public KeyBinding[] allKeys;
    @Shadow protected Minecraft mc;

    @Shadow protected abstract float parseFloat(String s);

    @Unique
    public KeyBinding sprintKey = new KeyBinding("key.sprint", Keyboard.KEY_LCONTROL);

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fov:F", opcode = Opcodes.PUTFIELD), method = "<init>*")
    public void init__fov(GameOptions instance, float value) {
        fov = 77/180f;
    }

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;gamma:F", opcode = Opcodes.PUTFIELD), method = "<init>*")
    public void init__gamma(GameOptions instance, float value) {
        gamma = 50/100f;
    }

    @Override
    @SuppressWarnings("all")
    public KeyBinding sprintKey() {
        return sprintKey;
    }

    @Unique
    FieldMap fields = new FieldMap() {{
        put("music", () -> musicVolume, value -> musicVolume = value);
        put("sound", () -> soundVolume, value -> soundVolume = value);
        put("invertYMouse", () -> invertYMouse, value -> invertYMouse = value);
        put("mouseSensitivity", () -> sensitivity, value -> sensitivity = value);
        put("fov", () -> fov, value -> fov = value);
        put("gamma", () -> gamma, value -> gamma = value);
        put("viewDistance", () -> renderDistance, value -> renderDistance = value);
        put("guiScale", () -> guiScale, value -> guiScale = value);
        put("particles", () -> particle, value -> particle = value);
        put("bobView", () -> bobView, value -> bobView = value);
        put("anaglyph3d", () -> anaglyph3d, value -> anaglyph3d = value);
        put("advancedOpengl", () -> advancedOpengl, value -> advancedOpengl = value);
        put("fpsLimit", () -> maxFramerate, value -> maxFramerate = value);
        put("difficulty", () -> difficultyLevel, value -> difficultyLevel = value);
        put("fancyGraphics", () -> fancyGraphics, value -> fancyGraphics = value);
        put("ao", () -> ambientOcculsion, value -> ambientOcculsion = value);
        put("clouds", () -> renderClouds, value -> renderClouds = value);
        put("skin", () -> currentTexturePackName, value -> currentTexturePackName = value);
        put("lastServer", () -> lastServer, value -> lastServer = value);
        put("lang", () -> language, value -> language = value);
        put("chatVisibility", () -> chatVisibility, value -> chatVisibility = value);
        put("chatColors", () -> chatColor, value -> chatColor = value);
        put("chatLinks", () -> chatLink, value -> chatLink = value);
        put("chatLinksPrompt", () -> chatLinkPrompt, value -> chatLinkPrompt = value);
        put("chatOpacity", () -> chatOpacity, value -> chatOpacity = value);
        put("serverTextures", () -> useServerTextures, value -> useServerTextures = value);
        put("snooperEnabled", () -> snopperEnabled, value -> snopperEnabled = value);
        put("fullscreen", () -> fullscreen, value -> fullscreen = value);
        put("enableVsync", () -> vsync, value -> vsync = value);
        put("hideServerAddress", () -> hideServerAddress, value -> hideServerAddress = value);
    }};

    @Inject(at = @At("HEAD"), method = "load", cancellable = true)
    public void load(CallbackInfo ci) {
        ci.cancel();

        try {
            if (!this.optionsFile.exists())
                return;

            BufferedReader reader = new BufferedReader(new FileReader(this.optionsFile));
            String raw = "";

            while ((raw = reader.readLine()) != null) {
                try {
                    String[] var3 = raw.split(" = ");

                    for (Map.Entry<String, Field<?>> entry : fields.entrySet()) {
                        String id = var3[0].trim();
                        String val = var3[1];

                        if (entry.getKey().equals(id)) {
                            Field<?> field = entry.getValue();

                            if (field.type() == Integer.class)
                                field.set(Integer.parseInt(val));
                            else if (field.type() == Float.class)
                                field.set(parseFloat(val));
                            else if (field.type() == Boolean.class)
                                field.set(val.equals("true"));
                            else field.set(val);
                        }
                    }

                    for (KeyBinding var7 : this.allKeys)
                        if (var3[0].equals("keyboard :: " + var7.translationKey))
                            var7.code = Integer.parseInt(var3[1]);
                } catch (Exception var8) {
                    Logger.getLogger("GameOptions").warning("Skipping bad option: " + raw);
                }
            }

            KeyBinding.updateKeysByCode();
            reader.close();
        } catch (Exception var9) {
            Logger.getLogger("GameOptions").severe("Failed to load options");
            var9.printStackTrace();
        }
    }

    @Inject(at = @At("HEAD"), method = "save", cancellable = true)
    public void save(CallbackInfo ci) {
        ci.cancel();

        try {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));

            int space = 0;
            for (String key : fields.keySet())
                space = Math.max(space, key.length());

            for (Map.Entry<String, Field<?>> entry : fields.entrySet()) {
                String id = entry.getKey();
                Object val = entry.getValue().get();

                var1.println(id + Strings.repeat(" ", space - id.length()) +
                                  " = " +
                                  val);
            }

            var1.println();
            for (KeyBinding var5 : this.allKeys)
                var1.println("keyboard :: " + var5.translationKey + " = " + var5.code);

            var1.close();
        } catch (Exception var6) {
            Logger.getLogger("GameOptions").severe("Failed to save options");
            var6.printStackTrace();
        }

        if (this.mc.playerEntity != null)
            this.mc.playerEntity.field_1667.sendPacket(new class_651(this.language, this.renderDistance, this.chatVisibility, this.chatColor, this.difficultyLevel));
    }
}
