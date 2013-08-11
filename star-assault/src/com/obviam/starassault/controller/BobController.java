package com.obviam.starassault.controller;

import com.obviam.starassault.model.Bob;
import com.obviam.starassault.model.World;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Ceryni
 * Date: 8/8/13
 * Time: 1:42 PM
 */
public class BobController {
    public static final long LONG_JUMP_PRESS = 150l;
    public static final float ACCELERATION = 20f;
    public static final float GRAVITY = -20f;
    public static final float MAX_JUMP_SPEED = 7f;
    public static final float DAMP = 0.90f;
    public static final float MAX_VEL = 5f;
    public static final float WIDTH = 10f;
    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.FIRE, false);
        keys.put(Keys.JUMP, false);
    }

    private World world;
    private Bob bob;
    private long jumpPressedTime;
    private boolean jumpingPressed;

    public BobController(World world) {
        this.world = world;
        this.bob = world.getBob();
    }

    public void update(float delta) {
        processInput();

        bob.getAcceleration().y = GRAVITY;
        bob.getAcceleration().scl(delta);
        bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);
        if (bob.getAcceleration().x == 0) bob.getVelocity().x *= DAMP;
        if (bob.getVelocity().x > MAX_VEL) {
            bob.getVelocity().x = MAX_VEL;
        }
        if (bob.getVelocity().x < -MAX_VEL) {
            bob.getVelocity().x = -MAX_VEL;
        }

        bob.update(delta);
        if (bob.getPosition().y < 0) {
            bob.getPosition().y = 0f;
            bob.setPosition(bob.getPosition());
            if (bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.IDLE);
            }
        }

        if (bob.getPosition().x < 0) {
            bob.getPosition().x = 0;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.IDLE);
            }
        }

        if (bob.getPosition().x > WIDTH - bob.getBounds().width) {
            bob.getPosition().x = WIDTH - bob.getBounds().width;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.IDLE);
            }
        }
    }

    private void processInput() {
        boolean movingLeft = keys.get(Keys.LEFT);
        boolean movingRight = keys.get(Keys.RIGHT);
        boolean jumping = keys.get(Keys.JUMP);

        if (jumping) {
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                bob.setState(Bob.State.JUMPING);
                bob.getVelocity().y = MAX_JUMP_SPEED;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
                        bob.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }

        if (movingLeft && movingRight) {
            bob.setFacingLeft(true);
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.IDLE);
            }
            bob.getAcceleration().x = 0;
            return;
        }

        if (movingLeft) {
            bob.setFacingLeft(true);
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.WALKING);
            }
            bob.getAcceleration().x = -ACCELERATION;
        } else if (movingRight) {
            bob.setFacingLeft(false);
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.WALKING);
            }
            bob.getAcceleration().x = ACCELERATION;
        } else{
            if (!bob.getState().equals(Bob.State.JUMPING)){
                bob.setState(Bob.State.IDLE);
            }
            bob.getAcceleration().x = 0;
        }
    }

    public void leftPressed() {
        keys.put(Keys.LEFT, true);
    }

    public void rightPressed() {
        keys.put(Keys.RIGHT, true);
    }

    public void firePressed() {
        keys.put(Keys.FIRE, true);
    }

    public void jumpPressed() {
        keys.put(Keys.JUMP, true);
        jumpingPressed = true;
    }

    public void leftReleased() {
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }

    public void fireReleased() {
        keys.put(Keys.FIRE, false);
    }

    public void jumpReleased() {
        keys.put(Keys.JUMP, false);
        jumpingPressed = false;
    }

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }
}
