package com.starseed.actors;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.starseed.util.Constants;

public class Flower extends Actor {
	// Damnit, this is getting ridiculous...
	// Unfortunately the lack of time produces some nasty anti-patterns like this.
	// No time to waste here anymore!
	private static TextureAtlas flowerAtlas1 = null;
	private static TextureAtlas flowerAtlas2 = null;
	private static TextureRegion getFlowerRegion( int playerIndex, int regionIndex ) {		
		TextureAtlas atlas = null;
		if( playerIndex == 1 ) {
			if( flowerAtlas1 == null ){
				flowerAtlas1 = new TextureAtlas( String.format(Constants.ATLAS_FLOWER_FORMAT, playerIndex) );
			}
			atlas = flowerAtlas1;
		}
		if( playerIndex == 2 ) {
			if( flowerAtlas2 == null ){
				flowerAtlas2 = new TextureAtlas( String.format(Constants.ATLAS_FLOWER_FORMAT, playerIndex) );
			}
			atlas = flowerAtlas2;
		}
		return atlas.findRegion( Constants.ATLAS_FLOWER_REGION, regionIndex );
	}
	
	private Vector2 asteroidPosition = new Vector2();
	private float asteroidRotation = 0f;
	private Vector2 flowerOffset = new Vector2(0,1);
	
	float frameWidth = 0f;
	float frameHeight = 0f;
	
	Animation bloomAnimation = null;
	float bloomTime = 0f;
	float scale = 1.0f;
	
	float delay = 0.0f;
	
	private static int startingAngle = 0;
	
	public Flower( float asteroidSize, int playerIndex )	{
		super();
		
		TextureRegion[] bloomFrames = new TextureRegion[Constants.ATLAS_FLOWER_FRAME_COUNT];
        for( int i = 0; i<Constants.ATLAS_FLOWER_FRAME_COUNT; i++ )
        {
        	bloomFrames[i] = getFlowerRegion(playerIndex, Constants.ATLAS_FLOWER_START_INDEX + i );    
        }
        bloomAnimation = new Animation(0.05f, bloomFrames);
        bloomAnimation.setPlayMode( PlayMode.NORMAL );

        Random rand = new Random();        
        float theFactor = rand.nextFloat();
        float distanceFactor = 0.6f + theFactor * 0.3f;
        scale = 0.10f * theFactor + 0.15f ;
        
        frameWidth = bloomFrames[0].getRegionWidth() * scale;
        frameHeight = bloomFrames[0].getRegionHeight() * scale;        
        
        flowerOffset.setLength( distanceFactor * asteroidSize );
		flowerOffset.setAngle( startingAngle + rand.nextInt(60) );
		startingAngle += 100;
		startingAngle %= 360;
		
		
		delay = rand.nextFloat() * 2f;        
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if( delay > 0.0f )
		{
			delay -= Gdx.graphics.getDeltaTime();
			return;
		}
		
		bloomTime += Gdx.graphics.getDeltaTime();	
		
		Vector2 finalOffset = new Vector2(flowerOffset);
		finalOffset.rotate( asteroidRotation * Constants.RAD_TO_DEG );
		
		batch.draw( 
				bloomAnimation.getKeyFrame(bloomTime, false), 
				asteroidPosition.x + finalOffset.x - 0.5f * frameWidth, asteroidPosition.y + finalOffset.y - 0.5f * frameHeight, 
				frameWidth * 0.5f, frameHeight * 0.5f,
				frameWidth, frameHeight, 
				1, 1, 
				finalOffset.angle() - 90f
			);		
	}

	public Vector2 getAsteroidPosition() {
		return asteroidPosition;
	}

	public void setAsteroidPosition(Vector2 asteroidPosition) {
		this.asteroidPosition = asteroidPosition;
	}

	public float getAsteroidRotation() {
		return asteroidRotation;
	}

	public void setAsteroidRotation(float asteroidRotation) {
		this.asteroidRotation = asteroidRotation;
	}
	
	
}