package com.starseed.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.starseed.box2d.AsteroidUserData;
import com.starseed.enums.AsteroidType;
import com.starseed.util.Constants;
import com.starseed.util.RandomUtils;
import com.starseed.util.SoundManager;

public class Asteroid extends GameActor {
	
	private TextureRegion asteroidFace;
	private AsteroidType aType;
	private int owningPlayer = 0;
	private int health = 0;
		
	private Array<Flower> flowers = new Array<Flower>();
	
	private static TextureAtlas asteroidAtlas = null;
	private static TextureRegion getAsteroidTexture( AsteroidType type, int health ) {
		if( asteroidAtlas == null )	{
			asteroidAtlas = new TextureAtlas(Constants.ATLAS_ASTEROID);
		}
		return asteroidAtlas.findRegion( type.getBaseRegionName(), type.getRegionIndex( health ) );
	}
	

	public Asteroid(Body body) {
		super(body);
		
		aType = getUserData().getAsteroidType();
		setHealth( aType.getHealth() );
	}

	@Override
	public AsteroidUserData getUserData() {
		return (AsteroidUserData) userData;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
				
		float scale = aType.getImageSize() / screenRectangle.width;
		
		if( owningPlayer != 0 )
		{
			batch.setColor( 0.55f, 0.95f, 0.55f, 1.0f);
		}
		batch.draw(
				asteroidFace, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
				screenRectangle.width, screenRectangle.height, 
				scale, scale, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
		if( owningPlayer != 0 )
		{
			batch.setColor( 1.0f, 1.0f, 1.0f, 1.0f);
		}
		
		Vector2 asteroidPosition = body.getPosition();
		asteroidPosition.scl(Constants.WORLD_TO_SCREEN);
		for( Flower flower : flowers ){
			flower.setAsteroidPosition(asteroidPosition);
			flower.setAsteroidRotation(body.getAngle());
			flower.draw(batch, parentAlpha);
		}
	}
	
	public boolean ensemenate( int playerIndex )
	{
		if( owningPlayer == 0 && !isDestroyed() )
		{
			owningPlayer = playerIndex;
			float asteroidSize = aType.getRadius() * Constants.WORLD_TO_SCREEN;
			int flowerCount = aType.getFlowerCount();
			for( int i = 0; i<flowerCount; i++ ){
				Flower flower = new Flower(asteroidSize, playerIndex, i, flowerCount);
				flowers.add(flower);
			}
			
			SoundManager.playSound( RandomUtils.randomElement(Constants.SOUND_ASTEROID_HIT), 0.15f );
			return true;
		}
		return false;
	}
	
	public void setHealth( int value ){
		health = MathUtils.clamp( value, 0, aType.getHealth());
		asteroidFace = getAsteroidTexture(aType, health); 
	}
	
	public void takeDamage()
	{
		SoundManager.playSound( Constants.SOUND_ASTEROID_HIT[ RandomUtils.nextInt(Constants.SOUND_ASTEROID_HIT.length) ] , 0.2f);
		setHealth(health-1);		
	}
	
	public boolean isDestroyed()
	{
		return health <= 0;
	}
	
	public int getOwningPlayer()
	{
		return owningPlayer;
	}
	
	public AsteroidType getAsteroidType()
	{
		return aType;
	}

}
