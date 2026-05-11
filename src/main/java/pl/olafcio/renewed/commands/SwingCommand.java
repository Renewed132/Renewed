package pl.olafcio.renewed.commands;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IntegratedConnection;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import pl.olafcio.renewed.mixininterface.ICommand;

import java.util.List;

public class SwingCommand
       extends AbstractCommand
       implements ICommand
{
    @Override
    public String getCommandName() {
        return "swing";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.swing.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length > 0 && args[0].length() > 1) {
            ServerPlayerEntity player = getPlayer(args[0]);
            if (player == null) {
                throw new PlayerNotFoundException();
            } else {
                player.method_3207();
                method_2890(source, "commands.swing.success", player.getTranslationKey());
            }
        } else {
            throw new IncorrectUsageException("commands.swing.usage");
        }
    }

    @Override
    public List method_3276(CommandSource source, String[] args) {
        return args.length >= 1 ? method_2894(args, MinecraftServer.getServer().getPlayerNames()) : null;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
