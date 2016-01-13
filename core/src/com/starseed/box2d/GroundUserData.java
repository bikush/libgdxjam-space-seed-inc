package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class GroundUserData extends UserData {

	public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }
	
}
