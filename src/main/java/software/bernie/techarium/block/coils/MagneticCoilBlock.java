package software.bernie.techarium.block.coils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import software.bernie.techarium.block.base.MachineBlock;
import software.bernie.techarium.item.WireItem;
import software.bernie.techarium.tile.magneticcoils.MagneticCoilTile;
import software.bernie.techarium.trait.block.BlockBehaviours;

public class MagneticCoilBlock extends MachineBlock<MagneticCoilTile> {
	
	public MagneticCoilBlock() {
		super(BlockBehaviours.magenticCoil, AbstractBlock.Properties.of(Material.METAL));
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		TileEntity t = world.getBlockEntity(pos);
		
		if (t == null) 
			return ActionResultType.PASS;
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
	
	private boolean isWireInHand(ItemStack inHand) {
		Item item = inHand.getItem();
		if (!(item instanceof WireItem)) return false;
		
		for (WireItem wire : WireItem.getWires()) {
			if (item == wire) return true;
		}
		
		return false;
	}
	
	private boolean placeTileCoil(World world, PlayerEntity player, MagneticCoilTile tileBlock) {
		ItemStack inHand = player.getMainHandItem();
		
		if (!isWireInHand(inHand) || !tileBlock.isCoilEmpty()) return false;
		MagneticCoilType type = ((WireItem) inHand.getItem()).getWireType(); 
		
		tileBlock.setCoilType(type);
		
		if (!world.isClientSide())
			inHand.shrink(1);
		return true;
		
	}	
}