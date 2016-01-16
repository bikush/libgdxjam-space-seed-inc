package com.starseed.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.ProjectileUserData;
import com.starseed.util.Constants;

public class Seed extends GameActor {
	
	TextureRegion seedTexture;
	
	public Seed(Body body) {
		super(body);
		seedTexture = Ship.getShipTextureRegion(Constants.ATLAS_SHIP_SEED_REGION, getPlayerIndex());
	}

	@Override
	public ProjectileUserData getUserData() {
		return (ProjectileUserData)userData;
	}
	
	public int getPlayerIndex()
	{
		return getUserData().getPlayerIndex();
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		batch.draw(
				seedTexture, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
				screenRectangle.width, screenRectangle.height, 
				4.0f, 4.0f, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
	}

}
