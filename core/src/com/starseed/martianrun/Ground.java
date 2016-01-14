package com.starseed.martianrun;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.actors.GameActor;
import com.starseed.box2d.UserData;
import com.starseed.util.Constants;

public class Ground extends GameActor {
	
	private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;
    private int speed = 10;

    public Ground(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.GROUND_IMAGE_PATH)));
        textureRegionBounds1 = new Rectangle(
        		0, 0,
        		transformToScreen( getUserData().getWidth() ),transformToScreen(getUserData().getHeight()));
        
        speed = (int) transformToScreen(speed);
    }

	@Override
	public GroundUserData getUserData() {
		return (GroundUserData)userData;
	}   
	
	@Override
    public void act(float delta) {
        super.act(delta);
        if (leftBoundsReached(delta)) {
            resetBounds();
        } else {
            updateXBounds(-delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y,
        		textureRegionBounds1.width, textureRegionBounds1.height);
        batch.draw(textureRegion, textureRegionBounds1.x + textureRegionBounds1.width, textureRegionBounds1.y,
        		textureRegionBounds1.width, textureRegionBounds1.height);
    }

    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds1.x + textureRegionBounds1.width - delta * speed) <= 0;
    }

    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
    }

    private void resetBounds() {
        textureRegionBounds1.x = 0;
    }

}