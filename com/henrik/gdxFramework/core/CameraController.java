package com.henrik.gdxFramework.core;

import com.badlogic.gdx.graphics.Camera;

/**
 * Created by Henri on 10/02/2015.
 */
public abstract class CameraController {
    protected Camera camera;
    public abstract void update();
    public void setCamera(Camera camera) { this.camera = camera; }
}
