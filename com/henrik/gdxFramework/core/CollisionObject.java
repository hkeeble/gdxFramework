package com.henrik.gdxFramework.core;

import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 28/01/2015.
 */
public class CollisionObject {
    private CollisionTag tag;
    private GameObject object;

    public CollisionObject(CollisionTag tag, GameObject object) {
        this.object = object;
        this.tag = tag;
    }

    /**
     * Returns the tag given to this collision object.
     */
    public CollisionTag getCollisionTag() {
        return tag;
    }

    /**
     * Returns a reference to the actual game object held in this collision object.
     */
    public GameObject getObject() {
        return object;
    }
}
