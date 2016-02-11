package com.starseed.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.ShipUserData;
import com.starseed.enums.AnimationType;
import com.starseed.util.AtlasUtils;
import com.starseed.util.Constants;
import com.starseed.util.OSUtils;
import com.starseed.util.SoundManager;

public class Ship extends GameActor {
	
	private int playerIndex = 0;
	
	private TextureRegion shipTexture = null;
	private boolean engineOn = false;
	private boolean turnLeft = false;
	private boolean turnRight = false;
	public float leftFactor = 1.0f;
	public float rightFactor = 1.0f;
	public float engineFactor = 1.0f;
	private float engineOnTime = 0.0f;
    public ShipType shipType = null;    

	private Animation exhaustAnimation = null;
	
	public enum ShipType {
		ORANGE_SHIP (1, "Mr. Orange", Input.Keys.W, 
				Input.Keys.A, Input.Keys.D, Input.Keys.CONTROL_LEFT, Input.Keys.SHIFT_LEFT),
		PURPLE_SHIP (2, "Ms. Purple", Input.Keys.UP,
				Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.CONTROL_RIGHT, Input.Keys.SHIFT_RIGHT);
		private int shipId, engineKey, turnLeftKey, turnRightKey, fireLaser, fireSeed;
		private String shipName;
		
		ShipType(int shipId, String shipName, int engineKey, 
				  int turnLeftKey, int turnRightKey, int fireLaser, int fireSeed) {
			//TODO: MAC ! ALT key
			int rightCtrl = Input.Keys.CONTROL_RIGHT;
			int altKey = Input.Keys.ALT_RIGHT;
			if (OSUtils.isMac()) { 
				engineKey = (engineKey == rightCtrl)? altKey: engineKey;
				turnLeftKey = (turnLeftKey == rightCtrl)? altKey: turnLeftKey;
				turnRightKey = (turnRightKey == rightCtrl)? altKey: turnRightKey;
				fireLaser = (fireLaser == rightCtrl)? altKey: fireLaser;
				fireSeed = (fireSeed == rightCtrl)? altKey: fireSeed;
			}
			this.shipId = shipId;
			this.shipName = shipName;
			this.engineKey = engineKey;
			this.turnLeftKey = turnLeftKey;
			this.turnRightKey = turnRightKey;
			this.fireLaser = fireLaser;
			this.fireSeed = fireSeed;
		}

		public int getShipId() {
			return shipId;
		}

		public int getEngineKey() {
			return engineKey;
		}

		public int getTurnLeftKey() {
			return turnLeftKey;
		}

		public int getTurnRightKey() {
			return turnRightKey;
		}

		public int getFireLaser() {
			return fireLaser;
		}

		public int getFireSeed() {
			return fireSeed;
		}

		public String getShipName() {
			return shipName;
		}
		
		public static ShipType findShipTypeByIndex(int playerIndex) {
			for (ShipType sType : ShipType.values()) {
				if (sType.getShipId() == playerIndex) {
					return sType;
				}
			}
			return null;
		}
	}
	
	public static TextureRegion getShipTextureRegion(String regionName, int index )
	{
		return AtlasUtils.getTextureAtlas(Constants.ATLAS_SHIP).findRegion( regionName, index );
	}

	public Ship(Body body, int playerIndex) {
		super(body);
		loadShip(ShipType.findShipTypeByIndex(playerIndex));
		
	}

	public Ship (Body body, ShipType sType) {
		super(body);
		loadShip(sType);
	}
	
	private void loadShip(ShipType sType) {
		shipType = sType;
		playerIndex = shipType.getShipId();
		shipTexture = getShipTextureRegion(Constants.ATLAS_SHIP_PLAYER_REGION, playerIndex);
		exhaustAnimation = AtlasUtils.getAnimation( AnimationType.EXHAUST );
	}
		
	public boolean moveKeyHandler(int keyCode, boolean make_move) {
		boolean retVal = false;
		if (keyCode == shipType.getEngineKey()) {
			this.setEngineOn(make_move);
			retVal = true;
		} else if (keyCode == shipType.getTurnRightKey()) {
			this.setTurnRight(make_move);
			retVal = true;
		} else if (keyCode == shipType.getTurnLeftKey()) {
			retVal = true;
			this.setTurnLeft(make_move);
		}
		return retVal;
	}
	
	@Override
	public ShipUserData getUserData() {
		return (ShipUserData) userData;
	}

	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		batch.draw(
				shipTexture, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
				screenRectangle.width, screenRectangle.height, 
				1.0f, 1.0f, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
		
		if( engineOn )
		{
			engineOnTime += Gdx.graphics.getDeltaTime();		
			Vector2 offset = new Vector2( 0, -screenRectangle.height * 0.5f );
			offset.setAngle( 90f + body.getAngle() * Constants.RAD_TO_DEG );
		
			batch.draw( 
					exhaustAnimation.getKeyFrame(engineOnTime, true), 
					screenRectangle.x - offset.x, screenRectangle.y - offset.y, 
					screenRectangle.width * 0.5f, screenRectangle.height * 0.5f,
					screenRectangle.width, screenRectangle.height * 2.f, 
					1.0f, 1.0f, 
					body.getAngle() * Constants.RAD_TO_DEG 
				);		
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if( engineOn )
		{			
			Vector2 force = new Vector2( engineFactor * Constants.SHIP_ENGINE_FORCE,0);
			force.setAngle( 90f + body.getAngle() * Constants.RAD_TO_DEG );
			body.applyForceToCenter(force, true);
		}
		if( turnLeft || turnRight )
		{
			body.applyAngularImpulse( ((turnLeft ? leftFactor * Constants.SHIP_ANGULAR_IMPULSE : 0) + (turnRight ? rightFactor * -Constants.SHIP_ANGULAR_IMPULSE : 0)), true);
		}		
	}
	 
	public void setEngineOn( boolean value )
	{
		engineOn = value;
		if( value )
		{
			SoundManager.playEngine(playerIndex);
			engineOnTime = 0.0f;
		}
		else
		{
			SoundManager.stopEngine(playerIndex);
		}
	}
	 
	public void setTurnLeft( boolean value ) 
	{
		turnLeft = value;
	}
	
	public void setTurnRight( boolean value ) 
	{
		turnRight = value;
	}
	
	public int getPlayerIndex()
	{
		return playerIndex;
	}
	
	public Vector2 getFrontOfShip()
	{
		Vector2 direction = getDirection();
		direction.setLength(getUserData().getHeight() * 0.5f);
		direction.add( body.getPosition() );
		return direction;
	}
	
	public Vector2 getDirection()
	{
		Vector2 direction = new Vector2( 0, 1 );
		direction.setAngle( 90f + body.getAngle() * Constants.RAD_TO_DEG );
		return direction;
	}
}
