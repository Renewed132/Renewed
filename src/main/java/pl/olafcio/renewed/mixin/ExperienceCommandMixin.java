package pl.olafcio.renewed.mixin;

import net.minecraft.command.AbstractCommand;
import net.minecraft.server.command.ExperienceCommand;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(ExperienceCommand.class)
public abstract class ExperienceCommandMixin
       extends AbstractCommand
{
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("experience");
    }
}
