package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class SeedUserData extends UserData {
	
	private int playerIndex = 0;

	public SeedUserData(float width, float height, int playerIndex) {
		super(width, height, UserDataType.SEED);
		this.playerIndex = playerIndex;
	}
	
	public int getPlayerIndex(){
		return playerIndex;
	}

}
