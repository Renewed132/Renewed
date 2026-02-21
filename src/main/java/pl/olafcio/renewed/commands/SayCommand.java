package pl.olafcio.renewed.commands;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class SayCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "say";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.say.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length == 0)
            throw new IncorrectUsageException("commands.say.usage");

        MinecraftServer.getServer().getPlayerManager().sendToAll(new ChatMessageS2CPacket(
                "[" + source.getUsername() + "] " +
                      method_2892(args, 0)
        ));
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
