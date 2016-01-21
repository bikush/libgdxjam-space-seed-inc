package com.starseed.actors;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.starseed.enums.AsteroidSizeType;
import com.starseed.util.AtlasUtils;
import com.starseed.util.Constants;
import com.starseed.util.RandomUtils;


public class AsteroidDebris extends Actor {

	private enum DebrisType {
		ROCK( "rock", 1, 6 ),
		DUST( "dust", 0, 2 );
		private String regionName;
		private int minIndex;
		private int maxIndex;
		DebrisType( String regionName, int min, int max ){
			this.regionName = regionName;
			this.minIndex = min;
			this.maxIndex = max;
		}
		public String getRegionName(){
			return regionName;
		}
		public int randomIndex(){
			return RandomUtils.rangeInt(minIndex, maxIndex);
		}
		public int validateIndex( int index ){
			return MathUtils.clamp(index, minIndex, maxIndex);
		}
	}
	
	private static TextureRegion getDebrisTexture( DebrisType type, int index ) {
		return AtlasUtils.getTextureAtlas(Constants.ATLAS_DEBRIS).findRegion( type.getRegionName(), type.validateIndex(index) );
	}

	private class DebrisPart {
		//DebrisType type;
		public float angle;
		public float angleSpeed;
		
		public Vector2 position;
		public Vector2 direction;
		public float speed;
		
		public TextureRegion texture;
		private float width;
		private float height;
		
		public float alpha;
		private float alphaSpeed;
		
		private static final float MAX_SPEED = 40f;
		private static final float MIN_SPEED = 20f;
		
		public DebrisPart( DebrisType type, int playerIndex, Vector2 position ){
			//this.type = type;
			if( type == DebrisType.DUST ){
				texture = getDebrisTexture(type, playerIndex);
			}else if( type == DebrisType.ROCK ){
				texture = getDebrisTexture(type, type.randomIndex());
			}
			if( texture != null )
			{
				width = texture.getRegionWidth();
				height = texture.getRegionHeight();
			}	
			
			Random rand = new Random();
			direction = new Vector2(1, 0);
			direction.setAngle( rand.nextFloat() * 360f );
			speed = rand.nextFloat() * (MAX_SPEED-MIN_SPEED) + MIN_SPEED;
			this.position = new Vector2(position);
			
			Vector2 offset = new Vector2( direction );
			offset.scl(rand.nextFloat() * 20f);
			this.position.add(offset);
			
			angle = rand.nextFloat() * 360f;
			angleSpeed = rand.nextFloat() * MAX_SPEED - MIN_SPEED;
			
			alpha = 1.0f;
			alphaSpeed = 0.20f + rand.nextFloat() * 0.1f;
		}
		
		public void act(float delta) {
			angle += angleSpeed * delta;
			
			Vector2 posDelta = new Vector2(direction);
			posDelta.scl(speed * delta);
			position.add( posDelta );
			
			alpha = Math.max(0.0f, alpha - alphaSpeed * delta);
			
			speed = Math.max( MIN_SPEED, speed - speed * 0.9f * delta );
		}
		
		public void draw(Batch batch, float parentAlpha) {
			if( texture != null ){
				batch.setColor(1f, 1f, 1f, parentAlpha * alpha);
				batch.draw(texture, position.x, position.y, width * 0.5f, height * 0.5f, width, height, 1.0f, 1.0f, angle);
				batch.setColor(1f, 1f, 1f, parentAlpha);
			}
		}
	}
	
	private Array<DebrisPart> debris = new Array<DebrisPart>();
	
	public AsteroidDebris( Asteroid asteroid ) {
		super();
		
		AsteroidSizeType aSize = asteroid.getAsteroidType().getSize();
		int playerIndex = asteroid.getOwningPlayer();
		Rectangle sRect = asteroid.getScreenRectangle();
		Vector2 position = new Vector2( sRect.x, sRect.y );
		
		int dustCount = aSize.getDebrisDustCount();
		int rockCount = aSize.getDebrisRockCount();
		
		for( int i = 0; i<dustCount; i++ ){
			DebrisPart part = new DebrisPart( DebrisType.DUST, playerIndex, position);
			debris.add(part);
		}
		for( int i = 0; i<rockCount; i++ ){
			DebrisPart part = new DebrisPart( DebrisType.ROCK, playerIndex, position);
			debris.add(part);
		}
				
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if( debris.size > 0 )
		{
			Array<DebrisPart> toRemove = new Array<DebrisPart>();
			for( DebrisPart part : debris ){
				part.act(delta);
				if( part.alpha <= 0.0f ){
					toRemove.add(part);
				}
			}
			debris.removeAll(toRemove, true);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if( debris.size > 0 )
		{
			for( DebrisPart part : debris ){
				part.draw(batch, parentAlpha);				
			}
		}
	}
	
	public boolean isFinished(){
		return debris.size == 0;
	}
	    
}
