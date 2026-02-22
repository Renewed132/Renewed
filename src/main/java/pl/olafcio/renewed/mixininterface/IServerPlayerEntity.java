package pl.olafcio.renewed.mixininterface;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IServerPlayerEntity {
    PlayerInventory getInventory();
    ItemEntity dropStack(ItemStack stack);

    default void giveItem(ItemStack stack) {
        Item item = stack.getItem();
        PlayerInventory inventory = getInventory();

        int empty = -1;
        for (int slot = 0; slot < inventory.main.length; slot++) {
            ItemStack slotStack = inventory.getInvStack(slot);

            if (slotStack == null) {
                if (empty == -1)
                    empty = slot;
            } else if ((
                        slotStack.getItem() == item &&
                        slotStack.count + stack.count <= item.getMaxCount()
            )) {
                slotStack.count += stack.count;
//                player.inventory.setInvStack(slot, stack);
                return;
            }
        }

        if (empty != -1) {
            inventory.setInvStack(empty, stack);
            return;
        }

        dropStack(stack);
    }
}
