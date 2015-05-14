package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.henrik.advergame.Game;

import java.util.ArrayList;

/**
 * A load state is responsible for managing the loading stage of a state.
 * Override this class and it's render function to add some custom loading animations.
 */
public class LoadState {

    private class ErrorListener implements AssetErrorListener {
        @Override
        public void error(AssetDescriptor asset, Throwable throwable) {
            errorAssets.add(asset.fileName);
            Gdx.app.log("Asset Loader: ", "Failed to load Game asset from: " + asset.fileName + ".");
            Gdx.app.log("Asset Loader: ", throwable.getMessage());
        }
    }

    protected HUD hud;
    private AssetManager assetManager;
    private AssetErrorListener errorListener;

    private ArrayList<String> errorAssets; // Assets that failed to loadGame

    private boolean done;

    public LoadState(AssetManager assetManager, HUD hud) {
        errorListener = new ErrorListener();
        done = false;

        errorAssets = new ArrayList<String>();

        this.hud = hud;
        this.assetManager = assetManager;
        this.assetManager.setErrorListener(errorListener);
    }

    public void initialize() {
        done = false;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.setErrorListener(errorListener);
    }

    public void addAsset(String path, Class<?> type) {
        assetManager.load(path, type);
        done = false;
    }

    public void addFont(String fontName, FreetypeFontLoader.FreeTypeFontLoaderParameter parameters) {
        assetManager.load(fontName, BitmapFont.class, parameters);
    }

    public void render() {

    }

    public void update() {
        if(!done) {
            if (assetManager.update()) {
                done = true;
            }
        }
    }

    public void unload() {
        assetManager.clear();
    }

    public void dispose() {

    }

    public void clear() {
        hud.clear();
    }

    public boolean isDone() {
        return done;
    }

}
