package pl.olafcio.renewed.mixin.commands.registry;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.commands.*;

@Mixin(CommandManager.class)
public class CommandManagerMixin extends CommandRegistry {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/command/AbstractCommand;setCommandProvider(Lnet/minecraft/command/CommandProvider;)V",
                    shift = At.Shift.BEFORE
            ),
            method = "<init>"
    )
    public void init(CallbackInfo ci) {
        this.registerCommand(new TellrawCommand());
        this.registerCommand(new DetachCommand());
        this.registerCommand(new SayCommand());
        this.registerCommand(new ClearCommand());
        this.registerCommand(new WeatherCommand());
    }
}
