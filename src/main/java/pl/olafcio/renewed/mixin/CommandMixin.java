package pl.olafcio.renewed.mixin;

import net.minecraft.command.Command;
import org.spongepowered.asm.mixin.Mixin;
import pl.olafcio.renewed.mixininterface.ICommand;

@Mixin(Command.class)
public interface CommandMixin
       extends ICommand {}
