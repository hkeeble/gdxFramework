package com.henrik.gdxFramework.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/**
 * Contains some helper methods for creating animations.
 */
public class AnimationUtils {

    /**
     * Create an animation.
     * @param spriteSheet The sprite sheet for the animation.
     * @param frameWidth The width of each frame.
     * @param frameHeight The height of each frame.
     * @param frameDuration The duration of each frame.
     * @param playMode The playmode of the animation.
     */
    public static Animation createAnimation(Texture spriteSheet, int frameWidth, int frameHeight, float frameDuration, Animation.PlayMode playMode) {
        TextureRegion[] frames = createFrameList(spriteSheet, frameWidth, frameHeight);
        Animation anim = new Animation(frameDuration, frames);
        anim.setPlayMode(playMode);
        return anim;
    }

    /**
     * Use this function to break a spritesheet into a list of texture regions, for use in an animated decal.
     * @param spriteSheet The sprite sheet to use.
     * @param frameWidth The width of each frame.
     * @param frameHeight The height of each frame.
     */
    public static TextureRegion[] createFrameList(Texture spriteSheet, int frameWidth, int frameHeight) {
        TextureRegion[][] tiles = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[tiles.length*tiles[0].length];

        int index = 0;
        for(int x = 0; x < tiles.length; x++) {
            for(int y = 0; y < tiles[0].length; y++) {
                frames[index] = tiles[x][y];
                index++;
            }
        }

        return frames;
    }

    /**
     * Use this function to break a spritesheet into a series of texture regions, each one representing a direction of movement.
     * @param spriteSheet The sprite sheet to use.
     * @param frameWidth The width of each frame.
     * @param frameHeight The height of each frame.
     */
    public static ArrayList<TextureRegion[]> createSprite(Texture spriteSheet, int frameWidth, int frameHeight) {
        ArrayList<TextureRegion[]> anims = new ArrayList<TextureRegion[]>();

        TextureRegion[][] tiles = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        for(int x = 0; x < tiles.length; x++) {
            TextureRegion[] frames = new TextureRegion[tiles[x].length];
            for(int y = 0; y < tiles[x].length; y++) {
                frames[y] = tiles[x][y];
            }
            anims.add(frames);
        }

        return anims;
    }

}
