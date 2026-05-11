package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Shadow
    private static void registerEntity(Class<?> clazz, String name, int id, int foregroundColor, int backgroundColor) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;registerEntity(Ljava/lang/Class;Ljava/lang/String;I)V"), method = "<clinit>")
    private static void registerEntity(Class<?> clazz, String name, int id, Operation<Void> original) {
        if (name.equalsIgnoreCase("SnowMan") || clazz == SnowGolemEntity.class)
            registerEntity(clazz, name, id, 0xACFCAC, 0x7CAC7C);
        else original.call(clazz, name, id);
    }
}
