package software.bernie.techarium.pipes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.Techarium;


@Mod.EventBusSubscriber(modid = Techarium.ModID)
public class CapabilityAttacher {

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<World> event) {
        if (!(event.getObject() instanceof ServerWorld))
            return;

        ICapabilityProvider provider = new ICapabilitySerializable<CompoundNBT>() {
            @Override
            public CompoundNBT serializeNBT() {
                LazyOptional<IPipeNetworkManagerCapability> capability = getCapability(PipeNetworkManagerCapability.INSTANCE);
                if (capability.isPresent()) {
                    IPipeNetworkManagerCapability networkManager = capability.orElseThrow(NullPointerException::new);
                    return networkManager.serializeNBT();
                }
                return new CompoundNBT();
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                LazyOptional<IPipeNetworkManagerCapability> capability = getCapability(PipeNetworkManagerCapability.INSTANCE);
                if (capability.isPresent()) {
                    IPipeNetworkManagerCapability networkManager = capability.orElseThrow(NullPointerException::new);
                    networkManager.deserializeNBT(nbt);
                }
            }

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == PipeNetworkManagerCapability.INSTANCE) {
                    return LazyOptional.of(PipeNetworkManagerCapability::new).cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(new ResourceLocation(Techarium.ModID, "network_manager"), provider);
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IPipeNetworkManagerCapability.class, new PipeNetworkManagerStorage(), PipeNetworkManagerCapability::new);
    }
}
