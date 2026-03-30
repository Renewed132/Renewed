package pl.olafcio.renewed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import pl.olafcio.renewed.api.RenewedExtension;
import pl.olafcio.renewed.api.internal.BlockFinalization;
import pl.olafcio.renewed.api.internal.ItemFinalization;
import pl.olafcio.renewed.api.registry.Registry;

import java.util.List;

public class Renewed implements ModInitializer {
    public static final Registry<Block> Blocks;
    public static final Registry<Item> Items;

    static {
        Blocks = new Registry<>();
        Items = new Registry<>();
    }

    @Override
    public void onInitialize() {
        List<EntrypointContainer<RenewedExtension>> extensions = FabricLoader.getInstance()
                                                                             .getEntrypointContainers("renewed:extension", RenewedExtension.class);

        for (EntrypointContainer<RenewedExtension> container : extensions) {
            container.getEntrypoint().onPrepare();
        }

        BlockFinalization.introduce();
        ItemFinalization.introduce();

        for (EntrypointContainer<RenewedExtension> container : extensions) {
            container.getEntrypoint().onReady();
        }
    }
}
