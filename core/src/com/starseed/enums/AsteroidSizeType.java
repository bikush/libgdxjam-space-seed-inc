package com.starseed.enums;

public enum AsteroidSizeType {
	
	SMALL( 2f, 1, 1250, 3, 0 ),
	MEDIUM( 4f, 2, 2000, 6, 1 ),
	LARGE( 6f, 4, 3000, 9, 2 );
	
	private float radius;
	private int flowerCount;
	private int points;
	private int health;
	private int crackCount;
	
	AsteroidSizeType( float radius, int flowerCount, int points, int health, int crackCount )
	{
		this.radius = radius;
		this.flowerCount = flowerCount;
		this.points = points;
		this.health = health;
		this.crackCount = crackCount;
	}

	public float getRadius() {
		return radius;
	}

	public int getFlowerCount() {
		return flowerCount;
	}

	public int getPoints() {
		return points;
	}

	public int getHealth() {
		return health;
	}	
	
	public int getCrackCount() {
		return crackCount;
	}		
	
}
