package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class AirSwimmingMoveControl extends MoveControl {

    private final int maxTurnX;
    private final int maxTurnY;
    private final float speed;
    private final boolean applyGravity;

    public AirSwimmingMoveControl(Mob mob, int maxTurnX, int maxTurnY, float speed, boolean applyGravity) {
        super(mob);
        this.maxTurnX = maxTurnX;
        this.maxTurnY = maxTurnY;
        this.speed = speed;
        this.applyGravity = applyGravity;
    }

    public void tick() {
        if (this.applyGravity) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
        }

        if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double) 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, (float) this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));

                this.mob.setSpeed(f1 * this.speed);
                double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                if (Math.abs(d1) > (double) 1.0E-5F || Math.abs(d4) > (double) 1.0E-5F) {
                    float f3 = -((float) (Mth.atan2(d1, d4) * (double) (180F / (float) Math.PI)));
                    f3 = Mth.clamp(Mth.wrapDegrees(f3), (float) (-this.maxTurnX), (float) this.maxTurnX);
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
                }

                float f6 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                float f4 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                this.mob.zza = f6 * f1;
                this.mob.yya = -f4 * f1;

            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }
}
