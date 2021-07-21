package software.bernie.techarium.block.coils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class MagneticCoilBlock extends MachineBlock<MagneticCoilTile> {

	public MagneticCoilBlock() {
		super(BlockBehaviours.magenticCoil, AbstractBlock.Properties.of(Material.METAL));
	}
}