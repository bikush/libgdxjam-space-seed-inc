package com.starseed.enums;

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

	public float getRadius() {
		return sizeType.getRadius();
	}

	public float getImageSize() {
		return imageSize;
	}

	public String getRegionName() {
		return String.format("asteroid_%s", sizeName);
	}
	
	public int getRegionIndex() {
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
