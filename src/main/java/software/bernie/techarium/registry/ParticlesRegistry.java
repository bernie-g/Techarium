package software.bernie.techarium.registry;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.client.particles.ArboretumParticles;

public class ParticlesRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Techarium.MOD_ID);
    
    public static final RegistryObject<BasicParticleType> ARBORETUM = registerParticleType("arboretum", () -> new BasicParticleType(true));
    
    private static <T extends ParticleType<?>> RegistryObject<T> registerParticleType(String name, Supplier<T> particleType) {
        RegistryObject<T> reg = PARTICLES.register(name, particleType);
        return reg;
    }
    
    public static void register(IEventBus bus){
    	PARTICLES.register(bus);
    }
    
    @SubscribeEvent
    public static void onRegisterParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.register(ARBORETUM.get(), ArboretumParticles.Factory::new);
    }
    
}
