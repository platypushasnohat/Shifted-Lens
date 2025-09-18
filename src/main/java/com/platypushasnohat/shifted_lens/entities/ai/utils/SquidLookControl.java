package com.platypushasnohat.shifted_lens.entities.ai.utils;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;

public class SquidLookControl extends SmoothSwimmingLookControl {

    public SquidLookControl(Mob mob) {
        super(mob, 10);
    }

    @Override
    protected boolean resetXRotOnTick() {
        return false;
    }

}
