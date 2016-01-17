package com.starseed.enums;

public enum AsteroidSizeType {
	
	SMALL( 	2f, 1, 1250, 3, 0, 4, 3 ),
	MEDIUM(	4f, 2, 2000, 6, 1, 7, 5 ),
	LARGE( 	6f,	4, 3000, 9, 2, 10, 8 );
	
	private float radius;
	private int flowerCount;
	private int points;
	private int health;
	private int crackCount;
	private int debrisDustCount;
	private int debrisRockCount;
	
	
	AsteroidSizeType( float radius, int flowerCount, int points, int health, int crackCount, int dustCount, int rockCount )
	{
		this.radius = radius;
		this.flowerCount = flowerCount;
		this.points = points;
		this.health = health;
		this.crackCount = crackCount;
		this.debrisDustCount = dustCount;
		this.debrisRockCount = rockCount;
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

	public int getDebrisDustCount() {
		return debrisDustCount;
	}

	public int getDebrisRockCount() {
		return debrisRockCount;
	}		
		
}
