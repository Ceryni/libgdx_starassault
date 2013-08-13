package com.obviam.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * User: Ceryni
 * Date: 8/7/13
 * Time: 8:36 PM
 */
public class Block {
    public static final float SIZE = 1f;

    Vector2 position = new Vector2();
    Rectangle bounds = new Rectangle();

    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
