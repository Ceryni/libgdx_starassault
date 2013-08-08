package com.obviam.starassault.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL10;
import com.obviam.starassault.controller.WorldController;
import com.obviam.starassault.model.World;
import com.obviam.starassault.view.WorldRenderer;

/**
 * User: Ceryni
 * Date: 8/7/13
 * Time: 8:48 PM
 */
public class GameScreen implements Screen, InputProcessor {
    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    private int width;
    private int height;

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        controller.update(v);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        renderer.setSize(width, height);
    }

    @Override
    public void show() {
        world = new World();
        renderer = new WorldRenderer(world, false);
        controller = new WorldController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.LEFT) {
            controller.leftPressed();
        }
        if (i == Input.Keys.RIGHT) {
            controller.rightPressed();
        }
        if (i == Input.Keys.Z) {
            controller.jumpPressed();
        }
        if (i == Input.Keys.X) {
            controller.firePressed();
        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        if (i == Input.Keys.LEFT) {
            controller.leftReleased();
        }
        if (i == Input.Keys.RIGHT) {
            controller.rightReleased();
        }
        if (i == Input.Keys.Z) {
            controller.jumpReleased();
        }
        if (i == Input.Keys.X) {
            controller.fireReleased();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android)){
            return false;
        }

        if (x < width / 2 && y > height / 2){
            controller.leftPressed();
        }
        if (x > width / 2 && y > height / 2){
            controller.rightPressed();
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android)){
            return false;
        }

        if (x < width / 2 && y > height / 2){
            controller.leftReleased();
        }
        if (x > width / 2 && y > height / 2){
            controller.rightReleased();
        }
        return true;

    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
