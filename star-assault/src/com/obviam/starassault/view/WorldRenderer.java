package com.obviam.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public static final float RUNNING_FRAME_DURATION = 0.06f;

    public static final float VIRTUAL_WIDTH = 480f;
    public static final float VIRTUAL_HEIGHT = 320f;
    public static final float ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
    private float width;
    private float height;

    private World world;
    private OrthographicCamera camera;

//    debug rendering
    ShapeRenderer debugRenderer = new ShapeRenderer();

    private TextureRegion blockTexture;
    private TextureRegion bobIdleLeft;
    private TextureRegion bobIdleRight;
    private TextureRegion bobFrame;

    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private float ppuX;
    private float ppuY;

    public void setSize (int w, int h){
        width = (float) w;
        height = (float) h;
        ppuX = width / CAMERA_WIDTH;
        ppuY = height / CAMERA_HEIGHT;
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
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/textures/textures.pack"));

        bobIdleLeft = atlas.findRegion("bob-01");

        bobIdleRight = new TextureRegion(bobIdleLeft);
        bobIdleRight.flip(true, false);
        blockTexture = atlas.findRegion("block");

        TextureRegion[] walkLeftFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++){
            walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);

        TextureRegion[] walkRightFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++){
            walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
            walkRightFrames[i].flip(true, false);
        }
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
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
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
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
        bobFrame = bob.isFacingLeft() ? bobIdleLeft : bobIdleRight;
        if(bob.getState().equals(Bob.State.WALKING)){
            bobFrame = bob.isFacingLeft()
                    ? walkLeftAnimation.getKeyFrame(bob.getStateTime(), true)
                    : walkRightAnimation.getKeyFrame(bob.getStateTime(), true);
        }

        spriteBatch.draw(
                bobFrame,
                bob.getPosition().x * ppuX,
                bob.getPosition().y * ppuY,
                Bob.SIZE * ppuX,
                Bob.SIZE * ppuY
        );
    }

    private void drawBlocks() {
        float newWidth = Block.SIZE * ppuX;
        float newHeight = Block.SIZE * ppuY;

        for (Block block : world.getBlocks()){
            spriteBatch.draw(
                    blockTexture,
                    block.getPosition().x * ppuX ,
                    block.getPosition().y * ppuY,
                    newWidth,
                    newHeight
            );
        }
    }
}
