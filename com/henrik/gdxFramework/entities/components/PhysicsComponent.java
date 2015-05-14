package com.henrik.gdxFramework.entities.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShapeChild;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.CollisionObject;
import com.henrik.gdxFramework.core.CollisionTag;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;


/**
 * A physics component can be used to give an object physical properties, and add it to a collision world.
 */
public class PhysicsComponent {

    private CollisionTag collisionTag = CollisionTags.STATIC_SOLID;

    private btCompoundShape collisionShape;
    private btCollisionObject collisionObject;
    
    private Vector3 objectVelocity = new Vector3();

    private boolean kinematic; // Use this to change whether or not an object is moveable at runtime
    private boolean collisionHandled; // Helper used in some cases, to ensure multiple collisions on a single frame are only handled once

    private Vector3 collisionPositionChanges = new Vector3();
    private int collisionPositionChangeCount = 0;

    private static Matrix4 idtMatrix = new Matrix4();
    
    /**
     * Create a new physics component given a shape and tag.
     * @param collisionShape The collision shape.
     * @param collisionTag The tag the component is given.
     */
    public PhysicsComponent(btCollisionShape collisionShape, CollisionTag collisionTag) {
        this.collisionShape = new btCompoundShape();
        this.collisionShape.addChildShape(idtMatrix, collisionShape);
        initCollisionObject();
        
        this.collisionTag = collisionTag;
        kinematic = false;
    }

    public PhysicsComponent(btCompoundShape collisionCompound, CollisionTag collisionTag) {
        this.collisionShape = collisionCompound;
        initCollisionObject();

        this.collisionTag = collisionTag;
        kinematic = false;
    }

    private void initCollisionObject() {
        collisionHandled = false;
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(this.collisionShape);
        collisionObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    }

    public boolean isKinematic() {
        return kinematic;
    }

    public void setKinematic(boolean kinematic) {
        this.kinematic = kinematic;
    }

    public btCollisionObject getCollisionObject() {
        return collisionObject;
    }

    public CollisionTag getCollisionTag() {
        return collisionTag;
    }

    public void setUserData(GameObject object) {
        collisionObject.userData = new CollisionObject(this.collisionTag, object);
    }

    public void update(GameObject object, World world) {

        collisionHandled = false;

        // Update position
        if(collisionPositionChangeCount > 0) {
            collisionPositionChanges.scl(1f / collisionPositionChangeCount);
            collisionPositionChanges.scl(-1f);
            object.translate(collisionPositionChanges);
            collisionPositionChanges.set(0,0,0);
            collisionPositionChangeCount = 0;
        }

        objectVelocity = object.getVelocity();
        collisionObject.setWorldTransform(object.getTransform());
    }

    public void addCollisionChange(Vector3 positionChange) {
        collisionPositionChanges.add(positionChange);
        collisionPositionChangeCount++;
    }

    public Vector3 getVelocity() {
        return objectVelocity;
    }

    public boolean isCollisionHandled() { return collisionHandled; }

    public void setCollisionHandled(boolean collisionHandled) { this.collisionHandled = collisionHandled; }

    public void setTransform(Matrix4 transform) {
        collisionObject.setWorldTransform(transform);
    }

    public void dispose() {
        collisionObject.dispose();      
        collisionShape.dispose();
    }
}
