package com.starseed.enums;

public enum AsteroidType {
	
	SMALL_1( 2f, 120f, "small", 1, 1, 1250 ),
	SMALL_2( 2f, 42f, "small", 2, 1, 1250 ),
	MEDIUM_1( 4f, 140f, "medium", 1, 2, 2000 ),
	LARGE_1( 6f, 195f, "large", 1, 4, 3000 ),
	LARGE_2( 6f, 165f, "large", 2, 4, 3000 );
	
	private float radius;
	private float imageSize;
	private String sizeName;
	private int index;
	private int flowerCount;
	private int points;
	
	AsteroidType( float radius, float imageSize, String sizeName, int index, int flowerCount, int points )
	{
		this.radius = radius;
		this.imageSize = imageSize;
		this.sizeName = sizeName;
		this.index = index;
		this.flowerCount = flowerCount;
		this.points = points;
	}

	public float getRadius() {
		return radius;
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
		return flowerCount;
	}
	
	public int getPoints() {
		return points;
	}
	
}
