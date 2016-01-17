package com.starseed.enums;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public enum AsteroidType {
	
	
	SMALL_1( AsteroidSizeType.SMALL, 120f, "small", 1 ),
	SMALL_2( AsteroidSizeType.SMALL, 42f, "small", 2 ),
	MEDIUM_1( AsteroidSizeType.MEDIUM, 140f, "medium", 1 ),
	LARGE_1( AsteroidSizeType.LARGE, 195f, "large", 1 ),
	LARGE_2( AsteroidSizeType.LARGE, 165f, "large", 2 );
	
	private AsteroidSizeType sizeType;
	private float imageSize;
	private String sizeName;
	private int index;
	
	AsteroidType( AsteroidSizeType sizeType, float imageSize, String sizeName, int index )
	{
		this.sizeType = sizeType;
		this.imageSize = imageSize;
		this.sizeName = sizeName;
		this.index = index;
	}
	
	public static AsteroidType getRandomType( AsteroidSizeType sType ){
		Array<AsteroidType> types = new Array<AsteroidType>();
		switch (sType) {
		case MEDIUM:
			types.add(MEDIUM_1);
			break;
		case LARGE:
			types.add(LARGE_1);
			types.add(LARGE_2);
			break;
		case SMALL:	
			types.add(SMALL_1);
			types.add(SMALL_2);	
			break;
		default:
			types.add(MEDIUM_1);
			types.add(LARGE_1);
			types.add(LARGE_2);
			types.add(SMALL_1);
			types.add(SMALL_2);	
			break;
		}
		return types.get( (new Random()).nextInt(types.size) );
	}
	
	public AsteroidSizeType getSize(){
		return sizeType;
	}

	public float getRadius() {
		return sizeType.getRadius();
	}

	public float getImageSize() {
		return imageSize;
	}
	
	public String getBaseRegionName(){
		return String.format("asteroid_%s_%d_crack", sizeName, index);
	}
		
	public int getRegionIndex( int health ) {
		int index = ((sizeType.getCrackCount() + 1) * (getHealth() - health)) / getHealth();
		index = MathUtils.clamp(index, 0, sizeType.getCrackCount());	// Safety check
		return index;
	}

	public int getFlowerCount() {
		return sizeType.getFlowerCount();
	}
	
	public int getPoints() {
		return sizeType.getPoints();
	}
	
	public int getHealth() {
		return sizeType.getHealth();
	}
	
}
