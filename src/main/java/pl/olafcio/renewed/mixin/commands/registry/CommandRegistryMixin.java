package pl.olafcio.renewed.mixin.commands.registry;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.olafcio.renewed.mixininterface.ICommandRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin implements ICommandRegistry {
    @Unique
    @Nullable
    private CommandSource currentSource;

    @Unique
    private final ExecutorService executor
            = Executors.newSingleThreadExecutor();

    @Override
    @SuppressWarnings("all")
    public CommandSource currentSource() {
        return currentSource;
    }

    @WrapMethod(method = "executeCommand")
    public void executeCommand(CommandSource source, String input, Operation<Void> original) {
        executor.submit(() -> {
            currentSource = source;
            original.call(source, input);
            currentSource = null;
        });
    }

    @Inject(at = @At("HEAD"), method = "method_3104", cancellable = true)
    private static void removeFirst(String[] strings, CallbackInfoReturnable<String[]> cir) {
        String[] cut = new String[strings.length - 1];

        System.arraycopy(strings, 1, cut, 0, strings.length - 1);

        cir.setReturnValue(cut);
    }
}
