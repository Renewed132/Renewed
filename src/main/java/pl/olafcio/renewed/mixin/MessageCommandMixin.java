package pl.olafcio.renewed.mixin;

import net.minecraft.server.command.MessageCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(at = @At("HEAD"), method = "getAliases", cancellable = true)
    public void getAliases(CallbackInfoReturnable<List<String>> cir) {
        //+whisper
        cir.setReturnValue(Arrays.asList("w", "whisper", "msg"));
    }
}
