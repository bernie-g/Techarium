package software.bernie.techarium.util;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class TechariumMaterial extends Material.Builder {
    public TechariumMaterial(MaterialColor materialColor) {
        super(materialColor);
    }

    public static final Material METAL = new TechariumMaterial(MaterialColor.METAL).flammable().build();
}