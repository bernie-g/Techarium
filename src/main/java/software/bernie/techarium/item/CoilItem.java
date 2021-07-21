package software.bernie.techarium.item;

import static software.bernie.techarium.registry.ItemGroupRegistry.TECHARIUM;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.item.Item;
import software.bernie.techarium.block.coils.MagneticCoilType;

public class CoilItem extends Item{

	@Getter
	private MagneticCoilType coilType;
	
	@Getter
	private static final List<CoilItem> coils = new ArrayList<CoilItem>();
	
	public CoilItem(MagneticCoilType coilType) {
		super(new Item.Properties().tab(TECHARIUM));
		this.coilType = coilType;
		coils.add(this);
	}
}
