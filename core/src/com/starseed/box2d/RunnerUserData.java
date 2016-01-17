package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class RunnerUserData extends UserData {
	
    public RunnerUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.RUNNER;
    }

}
