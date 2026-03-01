package pl.olafcio.renewed.mixininterface;

import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.command.InvalidNumberException;
import net.minecraft.command.SyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface ICommand {
    // ================== //
    // INPUT TO ESSENTIAL //
    // ================== //

    default int parseUnsignedInt(String value) {
        if (value.startsWith("+"))
            throw new InvalidNumberException("Expected an unsigned int");

        try {
            return Integer.parseUnsignedInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("Invalid unsigned integer");
        }
    }

    default ServerPlayerEntity getPlayer(String value) {
        MinecraftServer server = MinecraftServer.getServer();
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(value);

        return player;
    }

    // =================================== //
    // INPUT TO MINECRAFT REGISTRY PARSING //
    // =================================== //

    default Item getItem(String name) {
        return (Item) access(name, "item", (Translatable[]) Item.ITEMS, 1);
    }

    default Block getBlock(String name) {
        return (Block) access(name, "block", (Translatable[]) Block.BLOCKS, 0);
    }

    default Enchantment getEnchantment(String name) {
        return (Enchantment) access(name, "enchantment", (Translatable[]) Enchantment.ALL_ENCHANTMENTS, 0);
    }

    // ========= //
    // INTERNALS //
    // ========= //

    @NotNull
    @ApiStatus.Internal
    static <T extends Translatable> T access(String name, String registryName, T[] registry, int offset) {
        if (name.startsWith("+"))
            throw new InvalidNumberException("Invalid " + registryName + " ID");

        try {
            int id = Integer.parseUnsignedInt(name);
            T item = registry[id];

            if (item == null)
                throw new ArrayIndexOutOfBoundsException(StringUtil.capitalize(registryName) + " is 'null'");

            return item;
        } catch (NumberFormatException ignored) {
            // Try the name method then
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidNumberException(StringUtil.capitalize(registryName) + " ID out of bounds");
        }

        for (int i = offset; i < registry.length; i++) {
            T item = registry[i];
            if (item == null)
                continue;
            else if (
                    item.getUseKey() != null &&
                    item.getUseKey().split("\\.")[1].toLowerCase().equals(name)
            )
                return item;
        }

        throw new SyntaxException("Expected an " + registryName + " ID/name");
    }
}
