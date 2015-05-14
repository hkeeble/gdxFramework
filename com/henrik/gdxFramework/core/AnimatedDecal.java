package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;

/**
 * Used to represent a decal with an animation. Overrides decal texture regions with animations.
 */
public class AnimatedDecal extends Decal {

    private Animation currentAnimation; // The current animation being played
    private float time; // Time since animation start

    private boolean playing; // Whether or not the animation is playing
    private boolean isFinished;

    /**
     * Creates a new animated decal with a list of animations, that can be swapped between with {@link #setCurrentAnimation(int)}. Retain the indices of
     * specific animations elsewhere, so that they can be referred to easily.
     * @param width With of the decal in world coordinates.
     * @param height Height of the decal in world coordinates.
     * @param hasTransparency Whether or not the animation uses transparency.
     * @return
     */
    public static AnimatedDecal newAnimatedDecal (float width, float height, boolean hasTransparency) {
        return newAnimatedDecal(width, height, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND,
                hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
    }

    public static AnimatedDecal newAnimatedDecal (float width, float height, int srcBlendFactor, int dstBlendFactor) {
        AnimatedDecal decal = new AnimatedDecal();
        decal.setBlending(srcBlendFactor, dstBlendFactor);
        decal.dimensions.x = width;
        decal.dimensions.y = height;
        decal.setColor(1, 1, 1, 1);
        return decal;
    }

    /**
     * Sets the current animation on this decal.
     * @param animation The animation to set on this decal.
     */
    public void setCurrentAnimation(Animation animation) {
        if(currentAnimation == null) {
            currentAnimation = animation;
            setTextureRegion(currentAnimation.getKeyFrame(0));
        }
        else if(!currentAnimation.equals(animation)) {
            currentAnimation = animation;
            setTextureRegion(currentAnimation.getKeyFrame(0));
        }
    }

    /**
     * Returns the current animation being used by this decal.
     */
    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    /**
     * Pause the animation.
     */
    public void pause() {
        playing = false;
    }

    /**
     * Start the animation.
     */
    public void start() {
        isFinished = false;
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    /**
     * Reset the animation.
     */
    public void reset() {
        time = 0;
        isFinished = false;
        setTextureRegion(currentAnimation.getKeyFrame(time));
    }

    /**
     * Update the animation.
     */
    private void updateAnimation(float delta) {
        if(playing) {
            setTextureRegion(currentAnimation.getKeyFrame(time += delta));
            if(currentAnimation.isAnimationFinished(time)) {
                isFinished = true;
            }
            else
                isFinished = false;
        }
    }

    public void update() {
        updateAnimation(Gdx.graphics.getDeltaTime());
        super.update();
    }

    /**
     * Calculates the bounding box for this decal. Expensive due to divisions - avoid calculating in the render loop where possible.
     */
    public BoundingBox calculateBoundingBox() {
        Vector3 max = new Vector3(position.x-(dimensions.x/2), position.y-(dimensions.y/2), position.z-1);
        Vector3 min = new Vector3(position.x+(dimensions.x/2), position.y+(dimensions.y/2), position.z+1);

        return new BoundingBox(min, max);
    }

    /**
     * Returns whether or not the current animation has finished.
     */
    public boolean isAnimationFinished() {
        return isFinished;
    }
}
