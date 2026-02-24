package pl.olafcio.renewed.features.block;

import net.minecraft.block.Block;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import pl.olafcio.renewed.ShouldBeNamed;
import pl.olafcio.renewed.features.NewMaterial;

public class ConcreteBlock extends Block {
    public ConcreteBlock() {
        super(137, NewMaterial.CONCRETE);
        this.setItemGroup(ItemGroup.BUILDING_BLOCKS);
        this.setStrength(0.8F);
        this.setBlockSoundGroup(METAL_SOUND_GROUP);
    }

    @Override
    @ShouldBeNamed("getSprite")
    public int method_396(int i, int j) {
        // sprite arithmetic 😭🙏
        return 184 + (j/4)*16 + j%4;
    }

    @Override
    protected int method_431(int i) {
        return i;
    }

    public static int itemToBlockMETA(int i) {
        return ~i & 15;
    }
    public static int blockToItemMETA(int i) {
        return ~i & 15;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendItemStacks(int id, ItemGroup itemGroup, List list) {
        for (int var4 = 0; var4 < 16; ++var4) {
            list.add(new ItemStack(id, 1, var4));
        }
    }
}

