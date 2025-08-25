package com.platypushasnohat.shifted_lens.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WhirliwindParticle extends TextureSheetParticle {

    private static final int[] COLORS = {
            0xffffff, 0xeafdff
    };

    private WhirliwindParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, int variant) {
        super(world, x, y, z, motionX, motionY, motionZ);
        int color = selectColor(variant, this.random);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        setColor(r, g, b);
        this.quadSize *= 0.5F + this.random.nextFloat() * 0.5F;
        this.lifetime = 20 + this.random.nextInt(10);
        this.xd *= 0.1F;
        this.yd *= 0.1F;
        this.zd *= 0.1F;
        this.xd += motionX;
        this.yd += motionY;
        this.zd += motionZ;
        this.setAlpha(1);
    }

    public static int selectColor(int variant, RandomSource random) {
        return COLORS[random.nextInt(COLORS.length - 1)];
    }

    public void tick() {
        super.tick();
        float ageScale = age / (float) lifetime;
        this.setAlpha(1F - ageScale);
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            WhirliwindParticle particle = new WhirliwindParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 0);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}