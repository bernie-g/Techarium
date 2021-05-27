package software.bernie.techarium.item;

import lombok.Getter;
import net.minecraft.item.BlockItem;
import software.bernie.techarium.pipes.capability.PipeType;
import software.bernie.techarium.registry.BlockTileRegistry;

@Getter
public class PipeItem extends BlockItem {

    private final PipeType type;

    public PipeItem(PipeType type) {
        super(BlockTileRegistry.PIPE.getBlock(), new Properties());
        this.type = type;
    }

    @Override
    public String getTranslationKey() {
        return getDefaultTranslationKey();
    }
}
