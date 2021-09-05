package software.bernie.techarium.block.coils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import software.bernie.techarium.registry.ItemRegistry;

@AllArgsConstructor
public enum MagneticCoilType {
	TIER_NULL(3, "support"),
	TIER_1(5, "coppercoil"),
	TIER_2(8, "cobaltcoil"),
	TIER_3(12,"solariumcoil");
	
	@Getter
	private int power;
	
	@Getter
	private String name; //name for coils model texture	
}