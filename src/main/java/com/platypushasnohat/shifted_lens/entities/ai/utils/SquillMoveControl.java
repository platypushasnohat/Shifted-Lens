package com.platypushasnohat.shifted_lens.entities.ai.utils;

import com.platypushasnohat.shifted_lens.entities.Squill;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class SquillMoveControl extends MoveControl {

    private final Squill squill;
    private Vec3 prevPos;
    private int stuckTicks;

    public SquillMoveControl(Squill squill) {
        super(squill);
        this.prevPos = squill.position();
        this.squill = squill;
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speedIn) {
        super.setWantedPosition(x, y, z, speedIn);
        this.stuckTicks = 0;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            Vec3 pos = squill.position();
            double x = pos.x();
            double z = pos.z();
            Vec3 vector3d = new Vec3(this.wantedX - x, this.wantedY - pos.y(), this.wantedZ - z);
            double distance = vector3d.length();
            if (distance <= 0.2F) {
                this.operation = Operation.WAIT;
            } else {
                double dx = vector3d.x;
                double dz = vector3d.z;
                squill.setYRot(squill.yBodyRot = this.rotlerp(squill.getYRot(), (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F));
                float newMoveSpeed = Mth.lerp(0.125F, squill.getSpeed(), (float) (this.speedModifier * squill.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                squill.setSpeed(newMoveSpeed);
                double normalizedY = vector3d.y / distance;
                squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
                LookControl lookControl = squill.getLookControl();
                double d11 = lookControl.getWantedX();
                double d12 = lookControl.getWantedY();
                double d13 = lookControl.getWantedZ();
                double d8 = x + (dx / distance) * 2.0D;
                double d9 = squill.getEyeY() + normalizedY / distance;
                double d10 = z + (dz / distance) * 2.0D;
                if (!lookControl.isLookingAtTarget()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }

                lookControl.setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);

                if (this.prevPos.distanceToSqr(pos) <= 0.005F) {
                    if (++this.stuckTicks >= 60) {
                        this.operation = Operation.WAIT;
                    }
                } else {
                    this.stuckTicks = 0;
                }
            }
        } else {
            squill.setSpeed(0.0F);
            squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, 0.01F, 0.0F));
        }
        this.prevPos = squill.position();
    }
}
