package pl.olafcio.renewed.mixininterface;

import net.minecraft.command.InvalidNumberException;
import net.minecraft.command.SyntaxException;
import net.minecraft.item.Item;

public interface ICommand {
    default Item getItem(String name) {
        if (name.startsWith("+"))
            throw new InvalidNumberException("Invalid item ID");

        try {
            int id = Integer.parseUnsignedInt(name);
            Item item = Item.ITEMS[id];

            if (item == null)
                throw new ArrayIndexOutOfBoundsException("Item is 'null'");

            return item;
        } catch (NumberFormatException ignored) {
            // Try the name method then
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidNumberException("Item ID out of bounds");
        }

        for (int i = 1; i < Item.ITEMS.length; i++) {
            Item item = Item.ITEMS[i];
            if (item == null)
                break;
            else if (
                    item.getTranslationKey() != null &&
                    item.getTranslationKey().split("\\.")[1].toLowerCase().equals(name)
            )
                return item;
        }

        throw new SyntaxException("Expected an item ID/name");
    }
}
