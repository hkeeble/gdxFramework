package com.henrik.gdxFramework.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.henrik.gdxFramework.core.World;

import java.util.ArrayList;

/**
 * Created by Henri on 04/11/2014.
 */
public abstract class GameObject {

    private Matrix4 transform;
    private Vector3 velocity;

    static private Vector3 temp = new Vector3();
    
    public GameObject() {
        transform = new Matrix4();
        velocity = new Vector3(0,0,0);
        transform.idt(); // Set transform to identity
    }

    public Matrix4 getTransform() {
        return transform;
    }

    public Vector3 getPosition() {
        return transform.getTranslation(temp);
    }
    public void setPosition(float x, float y, float z) {
        transform.setTranslation(x,y,z);
    }
    public void setPosition(Vector3 position) { transform.setTranslation(position); }

    public void translate(Vector3 translation) {
        transform.translate(translation);
    }
    public void translate(float x, float y, float z) {
        transform.translate(x,y,z);
    }

    public Quaternion getRotation() {
        return transform.getRotation(new Quaternion(0,0,0,0));
    }
    public void setRotation(Vector3 axis, float degrees) {
        transform.setToRotation(axis, degrees);
    }

    public void rotate(Quaternion rotation) {
        transform.rotate(rotation);
    }

    public Vector3 getScale() {
        return transform.getScale(Vector3.Zero);
    }
    public void setScale(Vector3 scale) {
        transform.setToScaling(scale);
    }
    public void scale(Vector3 scale) {
        transform.scale(scale.x, scale.y, scale.z);
    }

    public Vector3 getVelocity() { return velocity; }
    public void setVelocity(Vector3 velocity) { this.velocity.set(velocity); }
    public void setVelocity(float x, float y, float z) { this.velocity.set(x,y,z); }

    public void update(World world) {
        transform.trn(this.velocity.x * Gdx.graphics.getDeltaTime(),
                        this.velocity.y * Gdx.graphics.getDeltaTime(),
                        this.velocity.z * Gdx.graphics.getDeltaTime());
    }

    public void render(World world) { }

    public abstract void dispose();
}