package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class ShipUserData extends UserData {

	public ShipUserData(float width, float height) {
		super(width, height);
		this.userDataType = UserDataType.PLAYER;
	}

}
