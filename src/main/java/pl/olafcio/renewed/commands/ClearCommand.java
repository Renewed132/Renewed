package pl.olafcio.renewed.commands;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.SyntaxException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import pl.olafcio.renewed.mixininterface.ICommand;

public class ClearCommand
       extends AbstractCommand
       implements ICommand
{
    @Override
    public String getCommandName() {
        return "clear";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.clear.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length == 0 || args.length > 2)
            throw new IncorrectUsageException("commands.clear.usage");

        MinecraftServer server = MinecraftServer.getServer();
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(args[0]);

        if (player == null)
            throw new SyntaxException("commands.clear.no_player");

        if (args.length == 2) {
            Item item = getItem(args[1]);

            int amount = 0;
            for (int i = 0; i < player.inventory.getInvSize(); i++) {
                ItemStack stack = player.inventory.getInvStack(i);
                if (stack != null && stack.getItem() == item) {
                    player.inventory.removeInvStack(i);
                    amount++;
                }
            }

            source.method_3331(source.translate("commands.clear.success.specific", amount, item.getName(), player.getUsername())); // sendMessage
        } else {
            int amount = 0;
            for (int i = 0; i < player.inventory.getInvSize(); i++) {
                if (player.inventory.getInvStack(i) != null) {
                    player.inventory.removeInvStack(i);
                    amount++;
                }
            }

            source.method_3331(source.translate("commands.clear.success.all", amount, player.getUsername())); // sendMessage
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
