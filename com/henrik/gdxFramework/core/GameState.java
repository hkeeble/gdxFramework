package com.henrik.gdxFramework.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Represents an abstract game component. Derive to create custom states.
 */
public abstract class GameState {
    protected GameBase game;

    private LoadState loadState;
    protected AssetManager assetManager;

    private boolean rendering = true; // Whether or not this component is rendering
    private boolean updating = true; // Whether or not this component is updating
    private boolean loading = true; // Whether or not this component is loading assets
    private boolean initialized = false;

    public GameState(GameBase game) {
        this.game = game;

        // Initialize the asset manager
        assetManager = new AssetManager();

        // Initialize the asset loadState for this state
        this.loadState = new LoadState(assetManager, game.hud);
    }

    protected void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public void setRendering(boolean isRendering) {
        this.rendering = isRendering;
    }
    public void setUpdating(boolean isUpdating) {
        this.updating = isUpdating;
    }

    public boolean isLoading() { return loading; }
    public boolean isRendering() {
        return rendering;
    }
    public boolean isUpdating() {
        return updating;
    }

    public void setEnabled(boolean isEnabled) {
        setRendering(isEnabled);
        setUpdating(isEnabled);
        initialized = false;
        loading = true;

        if(isEnabled) {
            loadState.initialize();
        }
    }

    public void addAsset(String name, Class<?> type) {
        loadState.addAsset(name, type);
    }

    public void addFont(String name, FreetypeFontLoader.FreeTypeFontLoaderParameter parameters) {
        loadState.addFont(name, parameters);
    }

    protected void initialize() {

    }

    protected void render() {
        if(!loadState.isDone())
            loadState.render();
    }

    protected void update() {
        if(!loadState.isDone()) {
            loadState.update();
        }
        else  {
            if(loading)
                loadState.clear();

            loading = false;
            if(!initialized) {
                initialize();
                initialized = true;
            }
        }
    }

    protected void dispose() {
        loadState.dispose();
        assetManager.dispose();
    }

    /**
     * Use clear to clear anything that is loaded on initialize. Clear will clear out the state before changing to a new state.
     */
    protected abstract void clear();

}