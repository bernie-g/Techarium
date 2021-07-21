package software.bernie.techarium.item;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.item.Item;
import software.bernie.techarium.block.coils.MagneticCoilType;

public class WireItem extends Item{

	@Getter
	private MagneticCoilType wireType;
	
	@Getter
	private static final List<WireItem> wires = new ArrayList<WireItem>();
	
	public WireItem(MagneticCoilType wireType) {
		super(new Item.Properties().tab(TECHARIUM));
		this.wireType = wireType;
		wires.add(this);
	}
}
