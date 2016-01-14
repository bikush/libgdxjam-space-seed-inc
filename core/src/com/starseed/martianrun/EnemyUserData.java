package com.starseed.martianrun;

import com.badlogic.gdx.math.Vector2;
import com.starseed.box2d.UserData;
import com.starseed.enums.UserDataType;

public class EnemyUserData extends UserData {

	private Vector2 linearVelocity;
	private String[] textureRegions;

    public EnemyUserData(float width, float height, String[] textureRegions) {
        super(width, height);
        userDataType = UserDataType.ENEMY;
        linearVelocity = MConstants.ENEMY_LINEAR_VELOCITY;
        this.textureRegions = textureRegions;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }
    
    public String[] getTextureRegions() {
        return textureRegions;
    }

}
