package com.henrik.gdxFramework.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Henri on 05/02/2015.
 */
public class SpriteRender {

    private Texture texture;
    private Vector2 position;

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public SpriteRender(Texture texture, Vector2 position) {
        this.texture = texture;
        this.position = position;

    }

}
