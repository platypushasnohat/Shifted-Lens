package com.platypushasnohat.shifted_lens.entities.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class SLGhastMoveControl extends MoveControl {

    public SLGhastMoveControl(Mob ghast) {
        super(ghast);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            final Vec3 vector3d = new Vec3(this.wantedX - mob.getX(), this.wantedY - mob.getY(), this.wantedZ - mob.getZ());
            final double length = vector3d.length();
            if (length < mob.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5D));
            } else {
                mob.setDeltaMovement(mob.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / length).multiply(0.5F, 0.5F, 0.5F)));
                final Vec3 vector3d1 = mob.getDeltaMovement();
                float f = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / (float) Math.PI;
                mob.setYRot(Mth.approachDegrees(mob.getYRot(), f, 1.0F));
                mob.yBodyRot = mob.getYRot();
            }
        }
    }
}