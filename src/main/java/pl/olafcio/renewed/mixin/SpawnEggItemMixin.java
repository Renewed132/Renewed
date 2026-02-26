package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.CommonI18n;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin extends Item {
    protected SpawnEggItemMixin(int i) {
        super(i);
    }

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void getDisplayName(ItemStack stack, CallbackInfoReturnable<String> cir) {
        String var2 = CommonI18n.translate(this.getTranslationKey() + ".name").trim();
        String var3 = EntityType.getEntityName(stack.getData());

        if (var3 != null)
            var2 = CommonI18n.translate("entity." + var3 + ".name") + " " + var2;

        cir.setReturnValue(var2);
    }

    @Unique
    private static final Random random
            = new Random();

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 1), method = "method_3456")
    private static boolean spawnEntity(World instance, Entity entity, Operation<Boolean> original) {
        if (entity instanceof SheepEntity)
            ((SheepEntity) entity).method_2862(random.nextInt(15));

        return original.call(instance, entity);
    }
}
