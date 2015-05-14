package com.henrik.gdxFramework.core;

import com.henrik.advergame.Game;
import com.henrik.advergame.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 12/11/2014.
 */
public class GameStateCollection {

    Map<Game.State,GameState> states;

    public GameStateCollection() {
        states = new HashMap<Game.State,GameState>();
    }

    public void add(Game.State id, GameState component) {
        states.put(id, component);
    }
    public void remove(int id) {
        states.remove(id);
    }

    /**
     * Enables the given component, and disables all other states.
     * @param id The component to enable.
     */
    public void enable(Game.State id) {
        Iterator it = states.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            GameState val = (GameState)pair.getValue();
            if(pair.getKey().equals(id)) {
                val.setEnabled(true);
            }
            else {
               val.setEnabled(false);
               val.clear();
            }
        }
    }

    public void update() {
        Iterator it = states.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            GameState val = (GameState)pair.getValue();
            if(val.isUpdating()) {
                val.update();
            }
            if(val.isRendering()) {
                val.render();
            }
        }
    }

    public GameState get(Game.State state) {
        return states.get(state);
    }

    public  void dispose() {
        Iterator it = states.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            GameState val = (GameState)pair.getValue();
            val.dispose();
        }
    }
}
