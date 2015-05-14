package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import com.henrik.advergame.Game;

import java.util.ArrayList;

/**
 * Created by Henri on 18/11/2014.
 */
public class HUD extends Stage {

	private OrthographicCamera camera;
    private Viewport viewport;
    private Table mainTable;

    private ArrayList<Table> tableList;
    private ArrayList<Label> labelList;
    private ArrayList<TextButton> buttonList;
    private ArrayList<Image> imageList;

    public static final int WIDTH = 480;
    public static final int HEIGHT = 600;

    public HUD() {
        super();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
        setViewport(viewport);

        tableList = new ArrayList<Table>();
        labelList = new ArrayList<Label>();
        buttonList = new ArrayList<TextButton>();
        imageList = new ArrayList<Image>();

        mainTable = new Table();
        mainTable.setFillParent(true);
        addActor(mainTable);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render() {

        // Set the main table to the forefront if neccesary
        if(mainTable.getZIndex() < getActors().size) {
            mainTable.setZIndex(getActors().size);
        }

        act();
        draw();

        if(Game.DEBUG_ACTIVE) {
            setDebugAll(true);
        }
    }

    public static TextButton makeTextButton(float x, float y, String text, EventListener event, Drawable buttonUp, Drawable buttonDown, Drawable buttonChecked, BitmapFont font) {
        TextButton button = new TextButton(text, new TextButton.TextButtonStyle(buttonUp, buttonDown, buttonChecked, font));
        button.setPosition(x, y);
        button.addListener(event);
        return button;
    }

    public static Button makeButton(float x, float y, EventListener event, Drawable buttonUp, Drawable buttonDown, Drawable buttonChecked) {
        Button button = new Button(new Button.ButtonStyle(buttonUp, buttonDown, buttonChecked));
        button.setPosition(x, y);
        button.addListener(event);
        return button;
    }

    public static Label makeLabel(float x, float y, Color color, String text, BitmapFont font) {
        Label label = new Label(text, new Label.LabelStyle(font, color));
        label.setPosition(x, y);
        return label;
    }

    public static Image makeImage(TextureRegion textureRegion) {
        Image image = new Image(textureRegion);
        return image;
    }

    public static AnimatedImage makeAnimatedImage(Animation animation) {
        AnimatedImage image = new AnimatedImage(animation);
        return image;
    }

    public Table getMainTable() {
        return mainTable;
    }

    /**
     * @param alpha The alpha to use.
     */
    public void setOpacity(float alpha) {
        Array<Actor> actors = getActors();
        for(Actor a : actors) {
            a.setColor(a.getColor().r, a.getColor().g, a.getColor().b, alpha);
        }
    }

    public void clear() {
        super.clear();

        mainTable = new Table();
        mainTable.setFillParent(true);
        addActor(mainTable);

        labelList.clear();
        buttonList.clear();
        tableList.clear();
        imageList.clear();
    }
}
