package pl.olafcio.renewed.commands;

import net.minecraft.command.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.olafcio.renewed.mixininterface.ICommand;

public class EnchantCommand
       extends AbstractCommand
       implements ICommand
{
    @Override
    public String getCommandName() {
        return "enchant";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.enchant.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length < 1 || args.length > 3)
            throw new IncorrectUsageException("commands.enchant.usage");

        ServerPlayerEntity player;
        if (args.length == 3) {
            player = getPlayer(args[2]);

            if (player == null)
                throw new PlayerNotFoundException("commands.enchant.no_player");
        } else if (source instanceof ServerPlayerEntity) {
            player = (ServerPlayerEntity) source;
        } else {
            throw new CommandException("commands.enchant.must_specify_player");
        }

        int level;
        if (args.length >= 2)
            level = parseUnsignedInt(args[1]);
        else level = 1;

        ItemStack stack = player.getMainHandStack();
        Enchantment enchant = getEnchantment(args[0]);

        if (stack == null)
            throw new CommandException("commands.enchant.cannot_enchant_air");

        stack.addEnchantment(enchant, level);

        if (args.length == 3)
            source.method_3331(source.translate(
                    "commands.enchant.success.other",
                    player.getUsername(),
                    stack.getItem().getDisplayName(stack),
                    enchant.getTranslatedName(level)
            )); // sendMessage
        else
            source.method_3331(source.translate(
                    "commands.enchant.success.self",
                    stack.getItem().getDisplayName(stack),
                    enchant.getTranslatedName(level)
            )); // sendMessage
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
