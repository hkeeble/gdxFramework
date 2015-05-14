package com.henrik.gdxFramework.entities.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 21/11/2014.
 */
public class ModelGraphicsComponent {
    ModelInstance model;
    BoundingBox boundingBox;
    BoundingBox transBoundingBox;

    Vector3 min;
    Vector3 max;

    public ModelGraphicsComponent(ModelInstance model) {
        this.model = model;

        min = new Vector3();
        max = new Vector3();

        // Use bounding box for frustum culling
        boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        transBoundingBox = new BoundingBox(boundingBox);
    }

    public void render(Camera camera, Renderer renderer, GameObject object) {
        model.transform.set(object.getTransform());

        boundingBox.getMin(min);
        boundingBox.getMax(max);
        transBoundingBox.set(min, max);

        // Move bounding box
        min.set(boundingBox.min);
        min.add(object.getPosition());
        max.set(boundingBox.max);
        max.add(object.getPosition());

        transBoundingBox.set(min, max);

        if(camera.frustum.boundsInFrustum(transBoundingBox)) {
            renderer.addModel(model);
        }
    }

    public ModelInstance getModel() {
        return model;
    }
}