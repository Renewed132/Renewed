package pl.olafcio.renewed.mixin;

import net.minecraft.util.ChatUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.gson.JsonElement;
import org.spongepowered.include.com.google.gson.JsonObject;
import org.spongepowered.include.com.google.gson.internal.Streams;
import org.spongepowered.include.com.google.gson.stream.JsonReader;
import pl.olafcio.renewed.ShouldBeNamed;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

@Mixin(targets = "net.minecraft.client.class_526")
public class ResourceDownloadThreadMixin {
    @Mutable @Shadow @Final
    @ShouldBeNamed("url")
    String field_1873;

    @Inject(at = @At("HEAD"), method = "run", cancellable = true)
    public void run(CallbackInfo ci) {
        // Idk why I just won't use the Mojang API but this is a little easier

        String nick = field_1873.substring(field_1873.lastIndexOf("/") + 1);
               nick = nick.substring(0, nick.length() - 4); // .png

        String profileUrl = "https://api.ashcon.app/mojang/v2/user/" + ChatUtil.stripTextFormat(nick);
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(profileUrl).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            if (succeeded(conn)) {
                InputStream inputStream = conn.getInputStream();
                JsonObject object = Streams.parse(new JsonReader(new InputStreamReader(inputStream)))
                                           .getAsJsonObject();

                if (field_1873.contains("/MinecraftSkins/")) {
                    // Skin
                    JsonObject textures = getProperty(object, "textures").getAsJsonObject();
                    JsonObject skin = getProperty(textures, "skin").getAsJsonObject();

                    field_1873 = getProperty(skin, "url").getAsJsonPrimitive()
                                                               .getAsString();
                } else if (field_1873.contains("/MinecraftCloaks/")) {
                    // Cape
                    JsonObject textures = getProperty(object, "textures").getAsJsonObject();
                    JsonObject skin = getProperty(textures, "cape").getAsJsonObject();

                    field_1873 = getProperty(skin, "url").getAsJsonPrimitive()
                                                               .getAsString();
                }
            } else {
                new Error("Server-failed profile query").printStackTrace();
                ci.cancel();
            }
        } catch (IOException e) {
            new Error("Failed to query profile URL", e).printStackTrace();
            ci.cancel();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            new Error("Failed to process profile query response", e).printStackTrace();
            ci.cancel();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    @Unique
    private JsonElement getProperty(JsonObject object, String name)
            throws IllegalAccessException, NoSuchFieldException
    {
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries)
            if (entry.getKey().equals(name))
                return entry.getValue();

        throw new RuntimeException("JSON property not found");
    }

    @Unique
    private boolean succeeded(HttpURLConnection conn) throws IOException {
        return conn.getResponseCode() / 100 != 4;
    }
}
