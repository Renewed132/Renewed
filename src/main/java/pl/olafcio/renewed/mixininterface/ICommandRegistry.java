package pl.olafcio.renewed.mixininterface;

import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.ApiStatus;

public interface ICommandRegistry {
    @ApiStatus.Internal
    CommandSource currentSource();
}
