package com.obviam.starassault.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.obviam.starassault.model.Block;
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

    private Array<Block> collidable = new Array<Block>();
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
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
        checkCollisionWithBlocks(delta);
        if (bob.getAcceleration().x == 0) bob.getVelocity().x *= DAMP;
        if (bob.getVelocity().x > MAX_VEL) {
            bob.getVelocity().x = MAX_VEL;
        }
        if (bob.getVelocity().x < -MAX_VEL) {
            bob.getVelocity().x = -MAX_VEL;
        }

        bob.update(delta);

        if (bob.getVelocity().y == 0 && bob.getState().equals(Bob.State.JUMPING)) {
            bob.setState(Bob.State.IDLE);
        }

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

        System.out.println(bob.getVelocity());
    }

    private void checkCollisionWithBlocks(float delta) {
        bob.getVelocity().scl(delta);
        Rectangle bobRect = rectPool.obtain();
        bobRect.set(bob.getBounds());

        int startX, endX;
        int startY = (int) bob.getBounds().y;
        int endY = (int) (bob.getBounds().y + bob.getBounds().height);

        if (bob.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(bob.getBounds().x + bob.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(bob.getBounds().x + bob.getBounds().width + bob.getVelocity().x);
        }

        populateCollidableBlocks(startX, startY, endX, endY);
        bobRect.x += bob.getVelocity().x;
        world.getCollisionRects().clear();
        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                bob.getVelocity().x = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        bobRect.x = bob.getPosition().x;
        startX = (int) bob.getBounds().x;
        endX = (int) (bob.getBounds().x + bob.getBounds().width);
        if (bob.getVelocity().y < 0) {
            startY = endY = (int) (bob.getBounds().y + bob.getVelocity().y);
        } else {
            startY = endY = (int) (bob.getBounds().y + bob.getBounds().height + bob.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);
        bobRect.y += bob.getVelocity().y;
        for (Block block : collidable) {
            if (block == null) continue;
            if (bobRect.overlaps(block.getBounds())) {
                if (bobRect.y + bobRect.height <= block.getBounds().y) {
                    jumpingPressed = false;
                }
                bob.getVelocity().y = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        bobRect.y = bob.getPosition().y;
        bob.getPosition().add(bob.getVelocity());
        bob.getBounds().x = bob.getPosition().x;
        bob.getBounds().y = bob.getPosition().y;
        bob.getVelocity().scl(1 / delta);

    }

    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < world.getLevel().getWidth() && y >= 0 && y < world.getLevel().getHeight()) {
                    collidable.add(world.getLevel().getBlocks()[x][y]);
                }
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
        } else {
            if (!bob.getState().equals(Bob.State.JUMPING)) {
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
