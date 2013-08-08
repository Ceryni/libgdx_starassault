package com.obviam.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public static final float CAMERA_WIDTH = 10f;
    public static final float CAMERA_HEIGHT = 7f;

    private World world;
    private OrthographicCamera camera;

//    debug rendering
    ShapeRenderer debugRenderer = new ShapeRenderer();

    private Texture bobTexture;
    private Texture blockTexture;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;
    private float ppuY;

    public void setSize (int w, int h){
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }

    public WorldRenderer(World world, boolean debug){
        this.world = world;
        this.camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.camera.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT /2f, 0);
        this.camera.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        bobTexture =  new Texture(Gdx.files.internal("data/bob_01.png"));
        blockTexture =  new Texture(Gdx.files.internal("data/block.png"));
    }

    public void render(){

        spriteBatch.begin();
        drawBlocks();
        drawBob();
        spriteBatch.end();
        if(debug){
            drawDebug();
        }
    }

    private void drawDebug() {
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

    private void drawBob() {
        Bob bob = world.getBob();
        spriteBatch.draw(
                bobTexture,
                bob.getPosition().x * ppuX,
                bob.getPosition().y * ppuY,
                Bob.SIZE * ppuX,
                Bob.SIZE * ppuY
        );
    }

    private void drawBlocks() {
        for (Block block : world.getBlocks()){
            spriteBatch.draw(
                    blockTexture,
                    block.getPosition().x * ppuX,
                    block.getPosition().y * ppuY,
                    Block.SIZE * ppuX,
                    Block.SIZE * ppuY
            );
        }
    }
}
