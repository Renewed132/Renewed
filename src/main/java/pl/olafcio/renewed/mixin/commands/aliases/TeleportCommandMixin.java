package pl.olafcio.renewed.mixin.commands.aliases;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin
       extends AbstractCommand
{
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("teleport");
    }
}
