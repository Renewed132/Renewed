package pl.olafcio.renewed.features.colorful.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.world.World;
import pl.olafcio.renewed.ShouldBeNamed;
import pl.olafcio.renewed.features.NewMaterial;

import java.util.List;
import java.util.Random;

public class ConcretePowderBlock extends Block {
	public ConcretePowderBlock() {
		super(139, NewMaterial.CONCRETE_POWDER);
		this.setItemGroup(ItemGroup.BUILDING_BLOCKS);
		this.setStrength(0.8F);
		this.setBlockSoundGroup(SAND_SOUND_GROUP);
	}

	@Override
	@ShouldBeNamed("getSprite")
	public int method_396(int i, int j) {
		return 256 + 16 + j;
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

	@Override
	public void breakNaturally(World world, int x, int y, int z) {
		world.method_3599(x, y, z, this.id, this.method_473());
	}

	@Override
	public void onNeighborUpdate(World world, int x, int y, int z, int neighborId) {
		world.method_3599(x, y, z, this.id, this.method_473());
	}

	@Override
	public void onTick(World world, int x, int y, int z, Random random) {
		if (!world.isClient)
			this.onServerTick(world, x, y, z);
	}

	private void onServerTick(World world, int x, int y, int z) {
		if (SandBlock.method_380(world, x, y - 1, z) && y >= 0) {
			byte dist = 32;
			if (SandBlock.field_318 || !world.isRegionLoaded(x - dist, y - dist, z - dist, x + dist, y + dist, z + dist)) {
				world.method_3690(x, y, z, 0);

				while (SandBlock.method_380(world, x, y - 1, z) && y > 0) {
					y--;
				}

				if (y > 0) {
					world.method_3690(x, y, z, this.id);
				}
			} else if (!world.isClient) {
				FallingBlockEntity fall = new FallingBlockEntity(
						world,
						x + 0.5F, y + 0.5F, z + 0.5F,
						this.id, field_439 /* block data */
				);

				world.spawnEntity(fall);
			}
		}
	}

	@Override
	public int method_473() {
		return 3;
	}
}
