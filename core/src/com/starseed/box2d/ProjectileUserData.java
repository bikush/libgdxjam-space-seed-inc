package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class ProjectileUserData extends UserData {
	
	private int playerIndex = 0;

	public ProjectileUserData(float width, float height, int playerIndex, boolean isSeed) {
		super( width, height, isSeed ? UserDataType.SEED : UserDataType.LASER );
		this.playerIndex = playerIndex;
	}
	
	public int getPlayerIndex(){
		return playerIndex;
	}

}
