package pl.olafcio.renewed.commands;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelProperties;
import org.jetbrains.annotations.NotNull;
import pl.olafcio.renewed.mixininterface.ICommand;

public class SetblockCommand
       extends AbstractCommand
       implements ICommand
{
    @Override
    public String getCommandName() {
        return "setblock";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.setblock.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length != 4)
            throw new IncorrectUsageException("commands.setblock.usage");

        if (!(source instanceof Entity))
            throw new CommandException("commands.setblock.entity_only");

        try {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);

            Block block = getBlock(args[3]);

            ((Entity) source).world.method_3652(x, y, z, block.id);  // setBlock

            source.method_3331(source.translate("commands.setblock.success", x, y, z, block.getTranslatedName())); // sendMessage
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("Expected an integer");
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
