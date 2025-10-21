package com.platypushasnohat.shifted_lens.mixin_utils;

import net.minecraft.world.entity.AnimationState;

public interface AnimationStateAccess {

    // fish
    AnimationState shiftedLens$getFlopAnimationState();
    AnimationState shiftedLens$getSwimmingAnimationState();

    // guardians
    AnimationState shiftedLens$getEyeAnimationState();
    AnimationState shiftedLens$getBeamAnimationState();

    // ghast
    AnimationState shiftedLens$getIdleAnimationState();
    AnimationState shiftedLens$getShootAnimationState();
}
