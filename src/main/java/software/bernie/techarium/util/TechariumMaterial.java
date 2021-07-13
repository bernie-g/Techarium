package software.bernie.techarium.util;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class TechariumMaterial extends Material.Builder {
    public TechariumMaterial(MaterialColor p_i48270_1_) {
        super(p_i48270_1_);
    }

    public static final Material METAL = (new TechariumMaterial(MaterialColor.METAL)).flammable().build();
}