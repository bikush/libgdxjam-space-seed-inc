package com.starseed.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.ShipUserData;
import com.starseed.util.Constants;

public class Ship extends GameActor {
	
	private TextureRegion shipTexture = null;
	private boolean engineOn = false;
	private boolean turnLeft = false;
	private boolean turnRight = false;

	public Ship(Body body) {
		super(body);
		
		shipTexture = new TextureRegion(new Texture(Gdx.files.internal(Constants.IMAGE_SHIP)));
	}

	@Override
	public ShipUserData getUserData() {
		return (ShipUserData) userData;
	}

	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		batch.draw(shipTexture, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
				screenRectangle.width, screenRectangle.height, 
				1.0f, 1.0f, 
				body.getAngle() * Constants.RAD_TO_DEG );
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if( engineOn )
		{
			Vector2 force = new Vector2( Constants.SHIP_ENGINE_FORCE,0);
			force.setAngle( 90f + body.getAngle() * Constants.RAD_TO_DEG );
			body.applyForceToCenter(force, true);
		}
		if( turnLeft || turnRight )
		{
			body.applyAngularImpulse( ((turnLeft ? Constants.SHIP_ANGULAR_IMPULSE : 0) + (turnRight ? -Constants.SHIP_ANGULAR_IMPULSE : 0)), true);
		}		
	}
	 
	public void setEngineOn( boolean value )
	{
		engineOn = value;
	}
	 
	public void setTurnLeft( boolean value ) 
	{
		turnLeft = value;
	}
	
	public void setTurnRight( boolean value ) 
	{
		turnRight = value;
	}
}
