package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

import java.util.ArrayList;

/**
 * World class. Represents an abstract world, containing a camera. Override to create custom worlds.
 */
public abstract class World {

    // Rendering
    protected Environment environment;
    protected Renderer renderer;

    protected Camera activeCamera;
    protected CameraController mainCameraController;
    protected PerspectiveCamera mainCamera;
    protected PerspectiveCamera debugCamera;
    protected FirstPersonCameraController debugCameraController;

    // Reference to game
    protected GameBase game;

    // Collision world
    private CollisionWorld collisionWorld;

    // Whether or not the world is in a paused state (prevents collision world from updating, use in derived class to pause game update logic)
    protected boolean paused;

    boolean debugCamEnabled;

    public World(GameBase game) {

        // Initialize the debug camera and controller
        debugCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Need to modify based on screen resolution...
        debugCamera.position.set(0f, 10f, 25f);
        debugCamera.lookAt(0, 0, 0);
        debugCamera.near = 1f;
        debugCamera.far = 300f;
        debugCamera.update();
        debugCameraController = new FirstPersonCameraController(debugCamera);
        game.addInputProcessor(debugCameraController);

        // Initialize the main camera
        mainCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Need to modify based on screen resolution...
        mainCamera.position.set(0f, 10f, 25f);
        mainCamera.lookAt(0, 0, 0);
        mainCamera.near = 1f;
        mainCamera.far = 1000f;
        mainCamera.update();

        this.game = game;
        renderer = new Renderer(activeCamera);
        environment = new Environment();

        // Set the active camera to the main camera
        activeCamera = mainCamera;
        renderer.setActiveCamera(activeCamera);

        collisionWorld = new CollisionWorld();

        debugCamEnabled = false;
    }

    public Camera getCamera() {
        return activeCamera;
    }

    public Camera getMainCamera() { return mainCamera; }

    public Environment getEnvironment() {
        return environment;
    }

    public Renderer getRenderer() { return  renderer; }

    public void toggleDebugCamera() {
        debugCamEnabled = !debugCamEnabled;
        if(debugCamEnabled) {
            activeCamera = debugCamera;
            renderer.setActiveCamera(activeCamera);
        } else {
            activeCamera = mainCamera;
            renderer.setActiveCamera(activeCamera);
        }
    }

    public void toggleCollisionDebug() {
        if(collisionWorld.isDebugActive())
            collisionWorld.disableDebug();
        else
            collisionWorld.enableDebug();
    }

    public boolean isCollisionDebugActive() {
        return collisionWorld.isDebugActive();
    }

    /**
     * Update all entity logic in this world.
     */
    public void update() {

        if(game.wasResized()) {
            activeCamera.viewportWidth = Gdx.graphics.getWidth();
            activeCamera.viewportHeight = Gdx.graphics.getHeight();
        }

        mainCameraController.update();
        mainCamera.update();

        if(debugCamEnabled) {
            debugCameraController.update();
            debugCamera.update();
        }

        if(!paused)
            collisionWorld.update(activeCamera);
    }

    public void render(boolean withEnvironment) {
        // Render all decals and models
        if(withEnvironment)
            renderer.render(environment);
        else
            renderer.render(null);

        renderer.clear();
    }

    public void dispose() {
        collisionWorld.dispose();
        renderer.dispose();
    }

    public void setMainCameraController(CameraController controller) {
        mainCameraController = controller;
        mainCameraController.setCamera(mainCamera);
    }

    public void setCollisionWorldDebugDrawMode(int mode) {
        collisionWorld.setDebugDrawMode(mode);
    }

    public CollisionObject[] rayTestAll(Vector3 start, Vector3 end, CollisionWorld.CollisionFilter group, CollisionWorld.CollisionFilter mask) { return collisionWorld.rayTestAll(start, end, group, mask); }

    public CollisionObject rayTestFirst(Vector3 start, Vector3 end, short group, short mask) { return collisionWorld.rayTestFirst(start, end, group, mask); }

    public void registerDynamicEntity(PhysicsComponent component, GameObject object) {
        collisionWorld.registerDynamicEntity(component, object);
    }

    public void registerStaticGeometry(PhysicsComponent component, GameObject object) {
        collisionWorld.registerStaticGeometry(component, object);
    }

    public void registerTriggerEntity(PhysicsComponent component, GameObject object) {
        collisionWorld.registerTriggerEntity(component, object);
    }

    public void unregisterCollisionObject(PhysicsComponent component) { collisionWorld.unregister(component); }

    public ArrayList<CollisionObject> getCollisionRegisteredObjects() {
        return collisionWorld.getRegisteredCollisionObjects();
    }

    public void clearCollisionWorld() {
        collisionWorld.clear();
    }

    public boolean isPaused() {
        return paused;
    }
}
