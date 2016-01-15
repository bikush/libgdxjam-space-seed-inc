package com.starseed.box2d;

import com.starseed.enums.UserDataType;

public class UserData {

	protected UserDataType userDataType;
	protected float width;
    protected float height;

    public UserData(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public UserData(float width, float height, UserDataType type) {
        this.width = width;
        this.height = height;
        this.userDataType = type;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public UserDataType getUserDataType() {
        return userDataType;
    }
    
}
