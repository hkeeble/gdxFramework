package com.henrik.gdxFramework.entities.components;

import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.components.ControllerComponent;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 04/11/2014.
 */
public class InputComponent extends ControllerComponent {
    protected InputHandler input;

    public InputComponent(InputHandler input) {
        super();
        this.input = input;
    }

    public void update(GameObject object, GameWorld world) {

    }

    @Override
    public void dispose() {

    }
}