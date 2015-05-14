package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 12/11/2014.
 */
public class InputHandler extends InputAdapter {

    private final int MAX_POINTERS = 2;
    private int mostRecentPointer = 0;

    class TouchData {
        private Vector2 position;
        private Vector2 dragDistance;
        private boolean touched;

        public TouchData() {
            position = Vector2.Zero;
            dragDistance = Vector2.Zero;
            touched = false;
        }

        public Vector2 getPosition() { return position; }
        public Vector2 getDragDistance() { return dragDistance; }
        public boolean isTouched() { return touched; }

        public void setPosition(Vector2 position) { this.position = position; }
        public void setDragDistance(Vector2 distance) { this.dragDistance = distance; }
        public void setTouched(boolean touched) {this.touched = touched; }
    }

    private Map<Integer,TouchData> touchData = new HashMap<Integer,TouchData>();

    public InputHandler() {
        for(int i = 0; i < MAX_POINTERS; i++ ) {
            touchData.put(i, new TouchData());
        }
    }

    /**
     * Gets the position of the most recently touched pointer.
     */
    public Vector2 getPosition() {
        return touchData.get(mostRecentPointer).getPosition();
    }

    /**
     * Gets whether or not the player is touching/clicking the screen.
     */
    public boolean isTouched() {
        for(int i = 0; i < MAX_POINTERS; i++) {
            if(touchData.get(i).isTouched() == true)
                return true;
        }
        return false;
    }

    public Vector2 getDrag() {
        return touchData.get(0).getDragDistance();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < MAX_POINTERS) {
            touchData.get(pointer).setPosition(new Vector2(screenX, screenY));
            touchData.get(pointer).setTouched(true);
            mostRecentPointer = pointer;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < MAX_POINTERS) {
            touchData.get(pointer).setPosition(new Vector2(screenX, screenY));
            touchData.get(pointer).setTouched(false);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(pointer < MAX_POINTERS) {
            Vector2 initialPos = touchData.get(pointer).getPosition();
            Vector2 dragDist = new Vector2(screenX - initialPos.x, screenY - initialPos.y);
            touchData.get(pointer).setDragDistance(dragDist);
        }

        return false;
    }

    /**
     * Use this to clear the input buffer.
     */
    public void clear() {
        Iterator iter = touchData.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            TouchData data = (TouchData)entry.getValue();
            data.setTouched(false);
        }
    }
}
