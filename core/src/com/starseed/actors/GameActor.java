package com.starseed.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.starseed.box2d.UserData;
import com.starseed.util.Constants;

public abstract class GameActor extends Actor {

    protected Body body;
    protected UserData userData;
    protected Rectangle screenRectangle;

    public GameActor(Body body) {
        this.body = body;
        this.userData = (UserData)body.getUserData();
        screenRectangle = new Rectangle();
    }
    
    public Body getBody() {
    	return this.body;
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);

        if (body.getUserData() != null) {
            updateRectangle();
        } else {
            // This means the world destroyed the body (enemy or runner went out of bounds)
            remove();
        }

    }
    
    private void updateRectangle() {
        screenRectangle.x = transformToScreen(body.getPosition().x - userData.getWidth() * 0.5f);
        screenRectangle.y = transformToScreen(body.getPosition().y - userData.getHeight() * 0.5f);
        screenRectangle.width = transformToScreen(userData.getWidth());
        screenRectangle.height = transformToScreen(userData.getHeight());
    }

    protected float transformToScreen(float n) {
        return Constants.WORLD_TO_SCREEN * n;
    }
    
    public Rectangle getScreenRectangle(){
    	return screenRectangle;
    }
    
    public abstract UserData getUserData();

}