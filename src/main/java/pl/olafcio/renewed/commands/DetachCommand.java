package pl.olafcio.renewed.commands;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IntegratedConnection;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetachCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "detach";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.detach.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length > 0 && args[0].length() > 1) {
            ServerPlayerEntity player = MinecraftServer.getServer().getPlayerManager().getPlayer(args[0]);
            if (player == null) {
                throw new PlayerNotFoundException();
            } else if (player.field_2823.connection instanceof IntegratedConnection) {
                throw new PlayerNotFoundException("commands.detach.self_lan");
            } else {
                player.field_2823.connection.close();
                method_2890(source, "commands.detach.success", player.getTranslationKey());
            }
        } else {
            throw new IncorrectUsageException("commands.detach.usage");
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
