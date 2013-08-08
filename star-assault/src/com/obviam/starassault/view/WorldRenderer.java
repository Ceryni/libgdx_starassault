package com.obviam.starassault.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.obviam.starassault.model.Block;
import com.obviam.starassault.model.Bob;
import com.obviam.starassault.model.World;

/**
 * User: Ceryni
 * Date: 8/7/13
 * Time: 8:59 PM
 */
public class WorldRenderer {
    private World world;
    private OrthographicCamera camera;

//    debug rendering
    ShapeRenderer debugRenderer = new ShapeRenderer();

    public WorldRenderer(World world){
        this.world = world;
        this.camera = new OrthographicCamera(10, 7);
        this.camera.position.set(5, 3.5f, 0);
        this.camera.update();
    }

    public void render(){
//        render blocks
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Rectangle);
        for (Block block : world.getBlocks()){
            Rectangle rectangle = block.getBounds();
            float x1 = block.getPosition().x + rectangle.x;
            float y1 = block.getPosition().y + rectangle.y;
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(x1, y1, rectangle.width, rectangle.height);
        }

        Bob bob = world.getBob();
        Rectangle rectangle = bob.getBounds();
        float x1 = bob.getPosition().x + rectangle.x;
        float y1 = bob.getPosition().y + rectangle.y;
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(x1, y1, rectangle.width, rectangle.height);
        debugRenderer.end();
    }
}
