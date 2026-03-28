package pl.olafcio.renewed.features.frostburn.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import pl.olafcio.renewed.ShouldBeNamed;

public class MagmaBlock extends Block {
    public MagmaBlock() {
        super(140, Material.STONE);
        this.setItemGroup(ItemGroup.BUILDING_BLOCKS);
        this.setStrength(0.5F);
        this.setBlockSoundGroup(STONE_SOUND_GROUP);
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        float var5 = 0.125F;
        return Box.getLocalPool().getOrCreate(x, y, z, x + 1, y + 1 - var5, z + 1);
    }

    @Override
    @ShouldBeNamed("getSprite")
    public int method_396(int i, int j) {
        int time = Math.toIntExact(Minecraft.getTime() / 1000 % 5);
        if (time > 2)
            time = 2 - (time - 2);

        return 188 + 16*time;
    }

    @Override
    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        entity.velocityX *= 0.3;
        entity.velocityZ *= 0.3;
        entity.setOnFireFor(1);
    }
}
