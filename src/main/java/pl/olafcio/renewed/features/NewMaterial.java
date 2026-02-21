package pl.olafcio.renewed.features;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class NewMaterial extends Material {
    public static final NewMaterial CONCRETE = new NewMaterial(MaterialColor.WEB);

    public NewMaterial(MaterialColor materialColor) {
        super(materialColor);
    }
}
