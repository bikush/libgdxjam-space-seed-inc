package com.starseed.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.AsteroidUserData;
import com.starseed.enums.AsteroidType;
import com.starseed.util.Constants;

public class Asteroid extends GameActor {
	
	private TextureRegion asteroidFace;
	private AsteroidType aType;
	
	private static TextureAtlas asteroidAtlas = null;
	private static TextureRegion getAsteroidTexture( AsteroidType type )
	{
		if( asteroidAtlas == null )
		{
			asteroidAtlas = new TextureAtlas(Constants.ASTEROID_ATLAS);
		}
		return asteroidAtlas.findRegion( type.getRegionName(), type.getRegionIndex() );
	}

	public Asteroid(Body body) {
		super(body);
		
		aType = getUserData().getAsteroidType();
		asteroidFace = getAsteroidTexture(aType);
        
	}

	@Override
	public AsteroidUserData getUserData() {
		return (AsteroidUserData) userData;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
				
		float scale = aType.getImageSize() / screenRectangle.width;
		
		batch.draw(
				asteroidFace, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
				screenRectangle.width, screenRectangle.height, 
				scale, scale, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
	}

}
