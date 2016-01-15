package com.starseed.box2d;

import com.starseed.enums.AsteroidType;
import com.starseed.enums.UserDataType;

public class AsteroidUserData extends UserData {
	
	private AsteroidType asteroidType;

	public AsteroidUserData(float width, float height, AsteroidType aType) {
		super(width, height);
		this.userDataType = UserDataType.ASTEROID;
		this.asteroidType = aType;
	}
	
	public AsteroidType getAsteroidType()
	{
		return asteroidType;		
	}

}
