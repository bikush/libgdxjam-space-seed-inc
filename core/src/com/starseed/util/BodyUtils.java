package com.starseed.util;

import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.UserData;
import com.starseed.enums.UserDataType;

public class BodyUtils {
	
	public static boolean bodyInBounds(Body body) {
        UserData userData = (UserData) body.getUserData();

        switch (userData.getUserDataType()) {
            case RUNNER:
            case ENEMY:
                return body.getPosition().x + userData.getWidth() / 2 > 0;
            case GROUND:
            default:
            		break;
        }

        return true;
    }
	
	public static boolean bodiesAreOfTypes( Body first, Body second, UserDataType typeOne, UserDataType typeTwo )
	{
		UserData userDataFirst = (UserData) first.getUserData();
		UserData userDataSecond = (UserData) second.getUserData();
		
		UserDataType typeFirst = userDataFirst != null ? userDataFirst.getUserDataType() : UserDataType.NONE;
		UserDataType typeSecond = userDataSecond != null ? userDataSecond.getUserDataType() : UserDataType.NONE;

        return (typeFirst == typeOne && typeSecond == typeTwo) || (typeFirst == typeTwo && typeSecond == typeOne);		
	}
	
	public static boolean bodiesAreOfType( Body first, Body second, UserDataType type )
	{
		return bodiesAreOfTypes(first, second, type, type);
	}

    public static boolean bodyIsOfType(Body body, UserDataType type) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == type;
    }
    
}