package pl.olafcio.renewed.mixin;

import net.minecraft.client.FolderTexturePack;
import net.minecraft.client.texture.AbstractTexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;

@Mixin(FolderTexturePack.class)
public class FolderTexturePackMixin
       extends AbstractTexturePack
{
    protected FolderTexturePackMixin(String string, String string2) {
        super(string, string2);
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/io/File;Ljava/lang/String;)V"), method = "openStream", cancellable = true)
    public void openStream__initFile(String path, CallbackInfoReturnable<InputStream> cir) {
        try {
            File var2 = new File(this.file, "assets/minecraft/" + path.substring(1));
            if (var2.exists())
                cir.setReturnValue(new BufferedInputStream(new FileInputStream(var2)));
        } catch (IOException ignored) {}
    }
}
