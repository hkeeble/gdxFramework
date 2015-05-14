package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 17/11/2014.
 */
public class CollisionWorld {

    protected btCollisionWorld collisionWorld;
    protected btCollisionConfiguration collisionConfiguration;
    protected btBroadphaseInterface broadPhaseInterface;
    protected btDispatcher dispatcher;
    protected ContactListener contactListener;
    protected DebugDrawer debugDraw;

    protected boolean debugActive;

    // Collision filter groups
    public enum CollisionFilter {
        STATIC((short)(1<<9)),
        DYNAMIC((short)(1<<8)),
        TRIGGER((short)(1<<7)),
        ALL((short)(-1));

        private final short value;

        CollisionFilter(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }
    }

    public CollisionWorld() {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadPhaseInterface = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadPhaseInterface, collisionConfiguration);
        contactListener = new ContactListener();
        debugDraw = new DebugDrawer();

        collisionWorld.setDebugDrawer(debugDraw);
        debugActive = false;
    }

    public void setContactListener(ContactListener contactListener) {
        if(this.contactListener != null) {
            this.contactListener.disable();
            this.contactListener.dispose();
        }

        this.contactListener = contactListener;
        contactListener.enable();
    }

    public void setDebugDrawMode(int mode) {
       debugDraw.setDebugMode(mode);
    }

    public void enableDebug() {
        debugActive = true;
    }

    public void disableDebug() {
        debugActive = false;
    }

    public boolean isDebugActive() { return debugActive; }

    public void enableContactListener() {
        if(this.contactListener != null)
            this.contactListener.enable();
    }

    public void disableContactListener() {
        if(this.contactListener != null)
            this.contactListener.disable();
    }

    public void update(Camera camera) {
        collisionWorld.performDiscreteCollisionDetection();

        if(debugActive) {
            debugDraw.begin(camera);
            collisionWorld.debugDrawWorld();
            debugDraw.end();
        }
    }

    /**
     * Register an object as a dynamic entity in the world. Dynamic entities can collide with static entities, and other dynamic entities.
     * @param component The physics component of the entity to add.
     * @param object The game object being registered, for use in callbacks (collision response).
     */
    public void registerDynamicEntity(PhysicsComponent component, GameObject object) {
        component.setUserData(object);
        collisionWorld.addCollisionObject(component.getCollisionObject(), CollisionFilter.DYNAMIC.getValue(), (short)(CollisionFilter.STATIC.getValue() | CollisionFilter.DYNAMIC.getValue() | CollisionFilter.TRIGGER.getValue()));
    }

    /**
     * Register an object as static geometry in the world. Static geometry cannot collide with one another, but will collide with dynamic entities.
     * @param component The physics component of the entity to add.
     * @param object The game object being registered, for use in callbacks (collision response).
     */
    public void registerStaticGeometry(PhysicsComponent component, GameObject object) {
        component.setUserData(object);
        component.getCollisionObject().setCollisionFlags(component.getCollisionObject().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
        collisionWorld.addCollisionObject(component.getCollisionObject(), CollisionFilter.STATIC.getValue(), CollisionFilter.DYNAMIC.getValue());
    }

    public void registerTriggerEntity(PhysicsComponent component, GameObject object) {
        component.setUserData(object);
        collisionWorld.addCollisionObject(component.getCollisionObject(), CollisionFilter.TRIGGER.getValue(), CollisionFilter.DYNAMIC.getValue());
    }

    public void unregister(PhysicsComponent component) {
        collisionWorld.removeCollisionObject(component.getCollisionObject());
    }

    public void clear() {

        int size = collisionWorld.getCollisionObjectArray().size();
        for(int i = 0; i < size; i++) {
            btCollisionObject obj = collisionWorld.getCollisionObjectArray().at(0);
            collisionWorld.removeCollisionObject(obj);
        }

        int s = collisionWorld.getCollisionObjectArray().size();

        collisionWorld.setDebugDrawer(debugDraw);
    }

    public void dispose() {
        collisionWorld.dispose();
        broadPhaseInterface.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
        contactListener.dispose();
    }

    /**
     * Ray test for the first object hit with the given filter mask.
     * @param start Start of the ray.
     * @param end End of the ray.
     * @return The first object colliding with the ray, or null if none.
     */
    public CollisionObject rayTestFirst(Vector3 start, Vector3 end, short group, short mask) {
        ClosestRayResultCallback callback = new ClosestRayResultCallback(start, end);
        callback.setCollisionFilterGroup(group);
        callback.setCollisionFilterMask(mask);
        collisionWorld.rayTest(start, end, callback);

        if(callback.hasHit()) {
            return (CollisionObject)callback.getCollisionObject().userData;
        }

        return null;
    }

    /**
     * Ray test for all objects hit with the given filter mask.
     * @param start The start of the ray.
     * @param end The end of the ray.
     * @param group The filter group the ray belongs to.
     * @param mask The filter mask the ray can collide with.
     * @return All objects colliding witht the ray, or null if there are none.
     */
    public CollisionObject[] rayTestAll(Vector3 start, Vector3 end, CollisionFilter group, CollisionFilter mask) {
        AllHitsRayResultCallback callback = new AllHitsRayResultCallback(start, end);
        callback.setCollisionFilterGroup(group.getValue());
        callback.setCollisionFilterMask(mask.getValue());
        collisionWorld.rayTest(start, end, callback);

        if(callback.hasHit()) {
            btCollisionObjectConstArray hits = callback.getCollisionObjects();

            CollisionObject[] hitObjects = new CollisionObject[hits.size()];

            for(int i = 0; i < hits.size(); i++) {
                hitObjects[i] = (CollisionObject)hits.at(i).userData;
            }

            return hitObjects;
        }

        return null;
    }

    public ArrayList<CollisionObject> getRegisteredCollisionObjects() {
        btCollisionObjectArray objs = collisionWorld.getCollisionObjectArray();

        ArrayList<CollisionObject> objArray = new ArrayList<CollisionObject>();

        for(int i = 0; i < objs.size(); i++) {
            objArray.add((CollisionObject)objs.at(i).userData);
        }

        return objArray;
    }
}
