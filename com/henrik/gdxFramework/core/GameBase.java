package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.henrik.advergame.Game;
import com.henrik.advergame.State;

import java.util.HashMap;

/**
 * The base game class from which the main game inherits.
 * @author Henri
 */
public class GameBase {

    public static final boolean DEBUG_ACTIVE = true;

    protected GameStateCollection states;
    protected InputHandler input;
    protected InputMultiplexer inputProcessor;

    // For debugging only
    protected Profiler profiler;
    protected ShapeRenderer debugRenderer;

    public HUD hud;

    protected AssetManager fontManager;

    protected boolean resized;

    private int fpsCap;

    // Game Fonts
    protected static HashMap<String,BitmapFont> fonts;

    public void create () {
        // Initialize list of game states
        states = new GameStateCollection();

        // Set the input handler for the game
        inputProcessor = new InputMultiplexer();
        input = new InputHandler();
        addInputProcessor(input);
        Gdx.input.setInputProcessor(inputProcessor);

        // Initialize toggleGLProfiling
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize debugging objects
        profiler = new Profiler();
        debugRenderer = new ShapeRenderer();

        // Initialize fonts
        fontManager = new AssetManager();

        // Enable FreetypeFontGenerator for loading .ttf fonts
        FileHandleResolver resolver = new InternalFileHandleResolver();
        fontManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        fontManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Create the game-wide HUD
        hud = new HUD();

        fonts = new HashMap<String,BitmapFont>();

        resized = false;

        addInputProcessor(hud);
    }

    public void setFPSCap(int fps) {
        this.fpsCap = fpsCap;
    }

    public void render () {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Update all current states
        states.update();

        // Update the profiler
        profiler.update();

        // Render the current HUD
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        hud.render();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        if(resized)
            resized = false;
    }

    public void enableProfiling() {
        profiler.enable();
    }

    public void disableProfiling() {
        profiler.disable();
    }

    public void toggleGLProfiling() {
        if(profiler.isEnabled())
            profiler.disable();
        else
            profiler.enable();
    }

    public boolean isGLProfilerEnabled() {
        return profiler.isEnabled();
    }

    public Profiler getProfile() {
        return profiler;
    }

    public void resize (int width, int height) {
        resized = true;
        hud.resize(width, height);
    }

    public InputHandler getInput() {
        return  input;
    }

    public void addInputProcessor(InputProcessor processor) { inputProcessor.addProcessor(processor); }

    public void enableState(Game.State state) {
        hud.clear(); // Clear the HUD for this state
        states.enable(state); // Enable the new state, disabling all other states
    }

    public ShapeRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public void pause () {

    }

    public boolean wasResized() {
        return resized;
    }

    public void resume () {

    }
    public void dispose () {
        states.dispose();
        fontManager.dispose();
    }

    public void loadFonts() { }

    public static BitmapFont getFont(String name, int size) {
        return fonts.get(name + String.valueOf(size));
    }

    public <T> T getState(Game.State state, Class<T> type) {
        return (T)states.get(state);
    }

}