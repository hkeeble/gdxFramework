package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Henri on 10/12/2014.
 */
public class Renderer {

    private ModelBatch modelBatch;
    private DecalBatch decalBatch;
    private SpriteBatch spriteBatch;
    private PointSpriteParticleBatch particleBatch;

    private ParticleSystem particleSystem;

    private ArrayList<ModelInstance> models;
    private ArrayList<SpriteRender> spriteRenders;

    Camera activeCamera;

    public Renderer(Camera camera) {
        modelBatch = new ModelBatch();
        decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
        spriteBatch = new SpriteBatch();

        // Set up particle renderer
        particleSystem = ParticleSystem.get();
        particleBatch = new PointSpriteParticleBatch();
        particleBatch.setCamera(camera);
        ParticleSystem.get().add(particleBatch);

        models = new ArrayList<ModelInstance>();
        spriteRenders = new ArrayList<SpriteRender>();
    }

    public void setActiveCamera(Camera camera) {
        this.activeCamera = camera;
        decalBatch.setGroupStrategy(new CameraGroupStrategy(camera));
    }

    public void render(Environment environment) {
        modelBatch.begin(activeCamera);
        for(ModelInstance model : models) {
            if(environment != null)
                modelBatch.render(model, environment);
            else
                modelBatch.render(model);
        }
        modelBatch.end();

        spriteBatch.begin();
        for(SpriteRender sprite : spriteRenders) {
            spriteBatch.draw(sprite.getTexture(), sprite.getPosition().x, sprite.getPosition().y);
        }
        spriteBatch.end();

        particleSystem.update();
        particleSystem.begin();
        particleSystem.draw();
        particleSystem.end();;
        modelBatch.render(particleSystem);

        decalBatch.flush();
    }

    /**
     * Renders the given model to a texture using the given camera.
     * @param model The model to render.
     * @param camera The camera to use to render the model
     * @return The resultant texture. Calling code takes ownership of the texture object.
     */
    public static Texture renderToTexture(Model model, Camera camera) {

        ModelBatch batch = new ModelBatch();

        ModelInstance instance = new ModelInstance(model);

        camera.position.set(new Vector3(0, 5, 0));
        camera.lookAt(0, 0, 0);

        // Render model to texture
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin(camera);
        batch.render(instance);
        batch.end();

        // Get the source texture from the frame buffer
        Pixmap source = ScreenUtils.getFrameBufferPixmap(0, 0, fbo.getWidth(), fbo.getHeight());

        // End of FBO use
        fbo.end();

        // Flip the pixmap, as FBOs have inverted Y Axis
        Pixmap pixmap = new Pixmap(source.getWidth(), source.getHeight(), Pixmap.Format.RGBA8888);
        for(int x = 0; x < pixmap.getWidth(); x++) {
            for(int y = 0; y < pixmap.getHeight(); y++) {
                pixmap.drawPixel(x, y, source.getPixel(x, y));
            }
        }

        // Decide on the width and height of the texture to pass back
        Vector2 bottomRight = new Vector2();
        for(int x = 0; x < pixmap.getWidth(); x++) {
            for(int y = 0; y < pixmap.getHeight(); y++) {
                Color c = new Color(pixmap.getPixel(x,y));
                if(c != new Color(0, 0, 0, 0)) {
                    bottomRight.set(x, y);
                }
            }
        }

        // Copy the appropriate region into a final pixmap
        Pixmap finalPixmap = new Pixmap((int)bottomRight.x, (int)bottomRight.y, Pixmap.Format.RGBA8888);
        finalPixmap.setColor(new Color(0, 0, 0, 0));
        finalPixmap.fill();
        for(int x = 0; x < bottomRight.x; x++) {
            for(int y = 0; y < bottomRight.y; y++) {
                finalPixmap.drawPixel(x, y, pixmap.getPixel(x, y));
            }
        }

        PixmapIO.writePNG(Gdx.files.external("test.png"), finalPixmap);

        // Dispose of any resources
        fbo.dispose();
        batch.dispose();

        // Pass back the texture
        return new Texture(pixmap);
    }


    public void clear() {
        models.clear();
        spriteRenders.clear();
    }

    public void addDecal(Decal decal) {
        decalBatch.add(decal);
    }

    public void addModel(ModelInstance model) {
        models.add(model);
    }

    public void addSprite(Texture texture, float x, float y) { spriteRenders.add(new SpriteRender(texture, new Vector2(x,y))); }

    public void addParticleEffect(ParticleEffect effect) { particleSystem.add(effect); }

    public SpriteBatch getSpriteBatch() { return spriteBatch; }

    public void dispose() {
        modelBatch.dispose();
        decalBatch.dispose();
        spriteBatch.dispose();
    }

}
