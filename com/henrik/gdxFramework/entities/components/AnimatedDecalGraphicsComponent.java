package com.henrik.gdxFramework.entities.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.henrik.gdxFramework.core.AnimatedDecal;
import com.henrik.gdxFramework.core.AnimationType;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.entities.GameObject;

import java.util.HashMap;

/**
 * Created by Henri on 12/03/2015.
 */
public class AnimatedDecalGraphicsComponent {

    private boolean billboard;

    protected AnimatedDecal decal;
    protected Animation animation;

    protected BoundingBox boundingBox;
    protected BoundingBox trnBoundingBox;

    protected boolean inView; // Whether or not this entity is within view

    protected float xRotation;

    public AnimatedDecalGraphicsComponent(float width, float height, Animation animation) {
        this(width, height, animation, true);
    }

    public AnimatedDecalGraphicsComponent(float width, float height, Animation animation, boolean billboard) {
        this.decal = AnimatedDecal.newAnimatedDecal(width, height, true);
        this.animation = animation;

        // Initialize animation to first in the list
        this.decal.setCurrentAnimation(animation);

        // Calculate the bounding box
        boundingBox = decal.calculateBoundingBox();

        xRotation = 0;

        inView = false;

        this.billboard = billboard;
    }

    public void start() {
        decal.start();
    }

    public void pause() {
        decal.pause();
    }

    public void reset() {
        decal.reset();
    }

    public boolean isPlaying() { return decal.isPlaying(); }

    public boolean isAnimationFinished() {
        return decal.isAnimationFinished();
    }

    public boolean isInView() { return inView; }

    public void setTint(Color color) { decal.setColor(color); }

    public Vector3 getPosition() { return decal.getPosition(); }

    public void render(Camera camera, Renderer renderer, GameObject object) {

        // Update the animation
        decal.update();

        // Calculate the current bound box from original and the object position
        trnBoundingBox = new BoundingBox();
        Vector3 min = new Vector3(), max = new Vector3();
        boundingBox.getMin(min);
        boundingBox.getMax(max);
        trnBoundingBox.set(min.add(object.getPosition()), max.add(object.getPosition()));

        // Only render if in view
        if(camera.frustum.boundsInFrustum(trnBoundingBox)) {

            if(billboard) {
                decal.lookAt(camera.position, camera.up);
            }

            decal.rotateX(xRotation);
            decal.setPosition(object.getPosition());
            renderer.addDecal(decal);
            inView = true;
        } else {
            inView = false;
        }
    }

    public void setxRotation(float rotation) { xRotation = rotation; }
}
