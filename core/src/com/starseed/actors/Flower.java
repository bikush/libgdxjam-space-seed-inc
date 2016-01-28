package com.starseed.actors;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.starseed.enums.AnimationType;
import com.starseed.util.AtlasUtils;
import com.starseed.util.Constants;
import com.starseed.util.SoundManager;

public class Flower extends Actor {
		
	private Vector2 asteroidPosition = new Vector2();
	private float asteroidRotation = 0f;
	private Vector2 flowerOffset = new Vector2(0,1);
	
	float frameWidth = 0f;
	float frameHeight = 0f;
	
	Animation bloomAnimation = null;
	float bloomTime = 0f;
	float scale = 1.0f;
	
	float delay = 0.0f;
	private boolean didPlaySound = false;
	
	public Flower( float asteroidSize, int playerIndex, int flowerNumber, int flowerCount )	{
		super();
		
        bloomAnimation = AtlasUtils.getAnimation( playerIndex == 1 ? AnimationType.BLOOM1 : AnimationType.BLOOM2 );
        
        Random rand = new Random();        
        float theFactor = rand.nextFloat();
        float distanceFactor = 0.6f + theFactor * 0.3f;
        scale = 0.10f * theFactor + 0.15f ;
        
        TextureRegion firstFrame = bloomAnimation.getKeyFrames()[0];
        frameWidth = firstFrame.getRegionWidth() * scale;
        frameHeight = firstFrame.getRegionHeight() * scale;        
        
        flowerOffset.setLength( distanceFactor * asteroidSize );
		flowerOffset.setAngle( flowerNumber * 360 / flowerCount + rand.nextInt(60) );		
		
		delay = flowerNumber * rand.nextFloat() * 2f / flowerCount;        
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if( delay > 0.0f )
		{
			delay -= Gdx.graphics.getDeltaTime();
			return;
		}
		if( !didPlaySound ){
			SoundManager.playSound(Constants.SOUND_BLOOMING);
			didPlaySound = true;
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