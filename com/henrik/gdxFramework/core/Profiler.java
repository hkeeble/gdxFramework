package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

/**
 * Created by Henri on 20/11/2014.
 */
public class Profiler {

    private int fps, glCalls, glTextureBindings, glDrawCalls, glShaderSwitches;
    private float glVertexCount;
    boolean enabled;

    public Profiler() {
        fps = 0;
        glCalls = 0;
        glTextureBindings = 0;
        glDrawCalls = 0;
        glShaderSwitches = 0;
        glVertexCount = 0;
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        GLProfiler.enable();
        enabled = true;
    }

    public void disable() {
        GLProfiler.reset();
        GLProfiler.disable();
        enabled = false;
    }

    public void toggle() {
        enabled = !enabled;
        if(enabled)
            GLProfiler.enable();
        else {
            GLProfiler.reset();
            GLProfiler.disable();
        }

    }

    public int getGlCalls() {
        return glCalls;
    }

    public int getGlTextureBindings() {
        return glTextureBindings;
    }

    public float getGlVertexCount() {
        return glVertexCount;
    }

    public int getGlDrawCalls() {
        return glDrawCalls;
    }

    public int getGlShaderSwitches() {
        return glShaderSwitches;
    }

    public int getFps() {
        return fps;
    }

    public void update() {
        if(enabled) {
            fps = Gdx.graphics.getFramesPerSecond();
            glCalls = GLProfiler.calls;
            glTextureBindings = GLProfiler.textureBindings;
            glDrawCalls = GLProfiler.drawCalls;
            glShaderSwitches = GLProfiler.shaderSwitches;
            glVertexCount = GLProfiler.vertexCount.value;

            GLProfiler.reset();
        }
    }
}
