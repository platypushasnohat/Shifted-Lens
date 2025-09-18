package com.platypushasnohat.shifted_lens.mixin_utils;

import net.minecraft.world.entity.AnimationState;

public interface AnimationStateAccess {

    // fish
    AnimationState getFlopAnimationState();
    AnimationState getSwimmingAnimationState();

    // guardians
    AnimationState getEyeAnimationState();
    AnimationState getBeamAnimationState();

    // ghast
    AnimationState getIdleAnimationState();
    AnimationState getShootAnimationState();
}
