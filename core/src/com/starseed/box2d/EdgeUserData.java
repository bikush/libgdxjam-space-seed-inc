package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class EdgeUserData extends UserData {

	public EdgeUserData(float width, float height) {
		super(width, height);
		this.userDataType = UserDataType.EDGE;
	}

}
