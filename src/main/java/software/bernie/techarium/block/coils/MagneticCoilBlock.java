package software.bernie.techarium.block.coils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.item.CoilItem;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class MagneticCoilBlock extends MachineBlock<MagneticCoilTile> {
	
	public MagneticCoilBlock() {
		super(BlockBehaviours.MAGNETIC_COIL, AbstractBlock.Properties.of(Material.METAL));
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		TileEntity t = world.getBlockEntity(pos);
		
		if (!(t instanceof MagneticCoilTile)) 
			return ActionResultType.PASS;
		
		MagneticCoilTile tileBlock = (MagneticCoilTile) t;
		
		if (removeTileCoil(player, tileBlock))
			return ActionResultType.SUCCESS;

		if (placeTileCoil(world, player, tileBlock))
			return ActionResultType.SUCCESS;
		
		
		return ActionResultType.PASS;
	}
	
	private boolean removeTileCoil(PlayerEntity player, MagneticCoilTile tileBlock) {
		if (!tileBlock.isCoilEmpty()) {
			tileBlock.removeCoil();
			return true;
		}
		return false;
	}
	
	private boolean isCoilInHand(ItemStack inHand) {
		Item item = inHand.getItem();
		if (!(item instanceof CoilItem)) return false;
		
		for (CoilItem wire : CoilItem.getCoils()) {
			if (item == wire) return true;
		}
		
		return false;
	}
	
	private boolean placeTileCoil(World world, PlayerEntity player, MagneticCoilTile tileBlock) {
		ItemStack inHand = player.getMainHandItem();
		
		if (!isCoilInHand(inHand) || !tileBlock.isCoilEmpty()) return false;
		MagneticCoilType type = ((CoilItem) inHand.getItem()).getCoilType(); 
		
		tileBlock.setCoilType(type);
		
		if (!world.isClientSide())
			inHand.shrink(1);
		return true;
		
	}	
}