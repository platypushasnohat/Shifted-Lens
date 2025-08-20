package com.platypushasnohat.shifted_lens.mixin_utils;

import net.minecraft.world.entity.AnimationState;

public interface GuardianAnimationAccess {
    AnimationState getIdleAnimationState();
    AnimationState getEyeAnimationState();
    AnimationState getBeamStartAnimationState();
    AnimationState getBeamAnimationState();
    AnimationState getBeamEndAnimationState();
}
