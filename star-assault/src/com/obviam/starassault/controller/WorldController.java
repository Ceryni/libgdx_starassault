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
public class WorldController {
    enum Keys{
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    private Bob bob;

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    static{
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.FIRE, false);
        keys.put(Keys.JUMP, false);
    }

    public WorldController(World world) {
        this.world = world;
        this.bob = world.getBob();
    }

    public void leftPressed(){
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed(){
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void firePressed(){
        keys.get(keys.put(Keys.FIRE, true));
    }

    public void jumpPressed(){
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void leftReleased(){
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased(){
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void fireReleased(){
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void jumpReleased(){
        keys.get(keys.put(Keys.JUMP, false));
    }
    
    public void update(float delta){
        processInput();
        bob.update(delta);
    }

    private void processInput() {
        boolean left = keys.get(Keys.LEFT);
        boolean right = keys.get(Keys.RIGHT);

        if(left){
            bob.setFacingLeft(true);
            bob.setState(Bob.State.WALKING);
            bob.getVelocity().x = -Bob.SPEED;
        }

        if(right){
            bob.setFacingLeft(false);
            bob.setState(Bob.State.WALKING);
            bob.getVelocity().x = Bob.SPEED;
        }

        if ((left && right) || (!left && !right)){
            bob.setState(Bob.State.IDLE);
            bob.getAcceleration().x = 0;
            bob.getVelocity().x = 0;
        }
    }
}
