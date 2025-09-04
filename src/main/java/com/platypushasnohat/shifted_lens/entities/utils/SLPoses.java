package com.platypushasnohat.shifted_lens.entities.utils;

import net.minecraft.world.entity.Pose;

public enum SLPoses {

    BEAM_START,
    BEAM,
    BEAM_END,
    SQUIRTING;

    public Pose get() {
        return Pose.valueOf(this.name());
    }
}
