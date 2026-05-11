package pl.olafcio.renewed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.ServerList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerList.class)
public class ServerListMixin {
    @Unique
    private static final NbtList EMPTY = new NbtList();

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;getList(Ljava/lang/String;)Lnet/minecraft/nbt/NbtList;"), method = "loadFile")
    public NbtList getList(NbtCompound instance, String string, Operation<NbtList> original) {
        return instance == null ? EMPTY : original.call(instance, string);
    }
}
