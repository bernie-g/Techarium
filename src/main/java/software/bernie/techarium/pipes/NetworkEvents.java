package software.bernie.techarium.pipes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.pipes.capability.IPipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerCapability;
import software.bernie.techarium.pipes.capability.PipeNetworkManagerStorage;
import software.bernie.techarium.pipes.networks.PipeNetwork;


@Mod.EventBusSubscriber(modid = Techarium.ModID)
public class NetworkEvents {

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<World> event) {
        if (!(event.getObject() instanceof ServerWorld))
            return;

        ICapabilityProvider provider = new ICapabilitySerializable<ListNBT>() {
            LazyOptional<IPipeNetworkManagerCapability> capability = LazyOptional.of(PipeNetworkManagerCapability::new);
            @Override
            public ListNBT serializeNBT() {
                LazyOptional<IPipeNetworkManagerCapability> capability = getCapability(PipeNetworkManagerCapability.INSTANCE);
                if (capability.isPresent()) {
                    IPipeNetworkManagerCapability networkManager = capability.orElseThrow(NullPointerException::new);
                    return networkManager.serializeNBT();
                }
                return new ListNBT();
            }

            @Override
            public void deserializeNBT(ListNBT nbt) {
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
                    return capability.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(new ResourceLocation(Techarium.ModID, "network_manager"), provider);
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IPipeNetworkManagerCapability.class, new PipeNetworkManagerStorage(), PipeNetworkManagerCapability::new);
    }

    //This way pipes don't need to tick, could prevent unnecessary lag
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side.isClient())
            return;
        event.world.getCapability(PipeNetworkManagerCapability.INSTANCE).ifPresent(manager -> manager.tick((ServerWorld) event.world));
    }
}
