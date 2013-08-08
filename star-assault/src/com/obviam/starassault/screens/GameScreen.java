package com.obviam.starassault.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.obviam.starassault.model.World;
import com.obviam.starassault.view.WorldRenderer;

/**
 * User: Ceryni
 * Date: 8/7/13
 * Time: 8:48 PM
 */
public class GameScreen implements Screen {
    private World world;
    private WorldRenderer renderer;

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    @Override
    public void resize(int i, int i2) {
        renderer.setSize(i, i2);
    }

    @Override
    public void show() {
        world = new World();
        renderer = new WorldRenderer(world, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
