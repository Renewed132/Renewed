package pl.olafcio.renewed.mixin;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.InvalidNumberException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.GiveCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.renewed.ShouldBeNamed;
import pl.olafcio.renewed.mixininterface.ICommand;

@Mixin(GiveCommand.class)
public abstract class GiveCommandMixin
       extends AbstractCommand
       implements ICommand
{
    @Shadow
    @ShouldBeNamed("getPlayerOrThrow")
    protected abstract PlayerEntity method_3785(String string);

    @Inject(at = @At("HEAD"), method = "execute", cancellable = true)
    public void execute(CommandSource source, String[] args, CallbackInfo ci) {
        ci.cancel();

        if (args.length >= 2) {
            PlayerEntity player = this.method_3785(args[0]);
            Item item = getItem(args[1]);

            int amount = 1;
            int damage = 0;

            if (args.length >= 3)
                amount = getClampedInt(source, args[2], 1, 64);

            if (args.length >= 4)
                damage = getInt(source, args[3]);

            ItemStack stack = new ItemStack(item, amount, damage);
            giveItem(player, stack);
            method_2890(source, "commands.give.success", item.getName(stack), item.id, amount, player.username);
        } else {
            throw new IncorrectUsageException("commands.give.usage");
        }
    }

    @Unique
    private void giveItem(PlayerEntity player, ItemStack stack) {
        Item item = stack.getItem();

        int empty = -1;
        for (int slot = 0; slot < player.inventory.main.length; slot++) {
            ItemStack slotStack = player.inventory.getInvStack(slot);

            if (slotStack == null) {
                if (empty == -1)
                    empty = slot;
            } else if (
                    slotStack.getItem() == item &&
                    slotStack.count + stack.count <= item.getMaxCount()
            ) {
                slotStack.count += stack.count;
//                player.inventory.setInvStack(slot, stack);
                return;
            }
        }

        if (empty != -1) {
            player.inventory.setInvStack(empty, stack);
            return;
        }

        player.dropStack(stack);
    }
}
