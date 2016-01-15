package com.starseed.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.UserData;
import com.starseed.util.Constants;

public class Seed extends GameActor {
	
	TextureRegion seedTexture;
	int playerIndex = 0;

	public Seed(Body body, int playerIndex) {
		super(body);
		this.playerIndex = playerIndex;
		seedTexture = Ship.getShipTextureRegion(Constants.ATLAS_SHIP_SEED_REGION, playerIndex);
	}

	@Override
	public UserData getUserData() {
		return userData;
	}
	
	public int getPlayerIndex()
	{
		return playerIndex;
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
