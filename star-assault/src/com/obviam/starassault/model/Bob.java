package com.obviam.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * User: Ceryni
 * Date: 8/7/13
 * Time: 8:31 PM
 */
public class Bob {

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void update(float delta) {
        //TODO: Finish the guts
    }

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    public static final float SPEED = 2f; //unit per second
    public static final float JUMP_VELOCITY = 1f;
    public static final float SIZE = 0.5f; //half a unit

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    State state = State.IDLE;
    boolean facingLeft = true;

    public Bob(Vector2 position){
        this.position = position;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }
}
