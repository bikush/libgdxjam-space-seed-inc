package com.starseed.martianrun;

import com.starseed.box2d.UserData;
import com.starseed.enums.UserDataType;

public class GroundUserData extends UserData {

	public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }
	
}
