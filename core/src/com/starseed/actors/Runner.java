package com.starseed.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.RunnerUserData;
import com.starseed.util.AtlasUtils;
import com.starseed.util.Constants;

public class Runner extends GameActor {
		
	private static Animation runningAnimation = null;
    private float stateTime;

    public Runner(Body body) {
        super(body);
        
        if( runningAnimation == null ){         
	        TextureAtlas textureAtlas = AtlasUtils.getTextureAtlas(Constants.CHARACTERS_ATLAS_PATH);
	        TextureRegion[] runningFrames = new TextureRegion[Constants.RUNNER_RUNNING_REGION_NAMES.length];
	        for (int i = 0; i < Constants.RUNNER_RUNNING_REGION_NAMES.length; i++) {
	            String path = Constants.RUNNER_RUNNING_REGION_NAMES[i];
	            runningFrames[i] = textureAtlas.findRegion(path);
	        }
	        runningAnimation = new Animation(0.1f, runningFrames);
        }
        stateTime = 0f;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.4f;
        float height = screenRectangle.height * 1.2f;
        
        // Running
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw(
        		runningAnimation.getKeyFrame(stateTime, true),
        		x, y, 
        		width * 0.5f, height * 0.5f, 
        		width, height, 
        		1.0f, 1.0f, body.getAngle() * Constants.RAD_TO_DEG);
    }

    
    @Override
	public RunnerUserData getUserData() {
		return (RunnerUserData)userData;
	}
  
}