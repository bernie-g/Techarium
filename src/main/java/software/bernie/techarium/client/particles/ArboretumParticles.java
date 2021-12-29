package software.bernie.techarium.client.particles;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArboretumParticles extends SpriteTexturedParticle {

	float particleMaxScale = 1;
	
    public ArboretumParticles(ClientWorld world, double posX, double posY, double posZ, double red,
                             double green, double blue) {
        super(world, posX, posY, posZ, red, green, blue);
        xd = (random.nextFloat() - 0.5f) / 50f;
        yd = random.nextFloat() / 50f;
        zd = (random.nextFloat() - 0.5f) / 50f;
        particleMaxScale = Math.max(random.nextFloat() / 120f, 0.0085f);
        quadSize = particleMaxScale;
        bCol = (float) blue;
        gCol = (float) green;
        rCol = (float) red;
        setLifetime(100);
        hasPhysics = true;
    }
    
    @Override
    public void render(IVertexBuilder builder, ActiveRenderInfo renderInfo, float partialTicks) {
        if (isAlive()) {
            Vector3d vec3d = renderInfo.getPosition();
            float f = (float) (MathHelper.lerp((double) partialTicks, this.xo, this.x) - vec3d.x());
            float f1 = (float) (MathHelper.lerp((double) partialTicks, this.yo, this.y) - vec3d.y());
            float f2 = (float) (MathHelper.lerp((double) partialTicks, this.zo, this.z) - vec3d.z());
            Quaternion quaternion;
            if (this.roll == 0.0F) {
                quaternion = renderInfo.rotation();
            } else {
                quaternion = new Quaternion(renderInfo.rotation());
                float f3 = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
                quaternion.mul(Vector3f.ZP.rotation(f3));
            }

            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.transform(quaternion);
            Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F),
                    new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
            float f4 = this.quadSize;

            for (int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.transform(quaternion);
                vector3f.mul(f4);
                vector3f.add(f, f1, f2);
            }

            float f7 = this.getU0();
            float f8 = this.getU1();
            float f5 = this.getV0();
            float f6 = this.getV1();
            int j = this.getLightColor(partialTicks);
            alpha = getGlowBrightness();
            builder.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z())
                    .uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(j).endVertex();
            builder.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z())
                    .uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(j).endVertex();
            builder.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z())
                    .uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(j).endVertex();
            builder.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z())
                    .uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(j).endVertex();
        }
    }

    @Override
    public void tick() {
    	float percentAge = 1 - (float) age / (float) getLifetime();
        quadSize = (float) (particleMaxScale * percentAge);
        yd += -0.001f;
        super.tick();
        
    }

    public float getGlowBrightness() {
        return 1 - (float) age / (float) getLifetime();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        int i = super.getLightColor(partialTicks);
        int j = 240;
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprite) {
            this.sprites = sprite;
        }

		@Override
		public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed) {
        	ArboretumParticles particle = new ArboretumParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);
            return particle;
		}
    }
}
