package pl.olafcio.renewed.mixin.commands.aliases;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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

    @ModifyConstant(constant = {
            @Constant(ordinal = 1, intValue = 0)
    }, method = "execute")
    public int execute__minimumY(int constant) {
        return -64;
    }
}
