package com.starseed.stages;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.Asteroid;
import com.starseed.actors.AsteroidDebris;
import com.starseed.actors.Laser;
import com.starseed.actors.Seed;
import com.starseed.actors.Ship;
import com.starseed.enums.UserDataType;
import com.starseed.util.BodyUtils;
import com.starseed.util.Constants;
import com.starseed.util.Pair;
import com.starseed.util.SoundManager;
import com.starseed.util.WorldUtils;

public abstract class GameStage extends Stage implements ContactListener, ContactFilter {
	protected Array<Seed> seeds = new Array<Seed>();
	protected World world;
	protected Array<Laser> lasers = new Array<Laser>();
	protected Array<AsteroidDebris> asteroidDebris = new Array<AsteroidDebris>();
	protected Array< Pair<Body,Body> > contactsToHandle = new Array< Pair<Body,Body> >();
	protected HashMap<Pair<UserDataType, UserDataType>, Boolean> contactMap = new HashMap<Pair<UserDataType,UserDataType>, Boolean>();
	protected Array<Asteroid> asteroids = new Array<Asteroid>();
	protected Ship player1 = null;
	protected Ship player2 = null;
	
	public GameStage() {
		super(new FitViewport(
				  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		setupContactMap();
	}
	
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw() {
		super.draw();
		//renderer.render(world, camera.combined);
	}
	
	private void setupContactMap() {
		contactMap.clear();
		
		// Asteroid contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.ASTEROID), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.SEED), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.EDGE), false);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.RUNNER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.LASER), true);
		
		// Seed contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.SEED), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.EDGE), false);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.RUNNER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.LASER), false);
		
		// Player ship contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.EDGE), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.RUNNER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.LASER), true);
		
		// Edge contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.EDGE, UserDataType.RUNNER), false);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.EDGE, UserDataType.LASER), false);
		
	}
	
	protected void createSeed( Ship sourceShip ) {		
		int playerIndex = sourceShip.getPlayerIndex();
		Vector2 position = sourceShip.getFrontOfShip();
		Vector2 direction = sourceShip.getDirection();
		float offset = Constants.SEED_RADIUS * 1.05f;
		position.add( direction.scl(offset, offset));
		
		Seed newSeed = new Seed( WorldUtils.createSeed(world, position, direction, playerIndex) );
		addActor(newSeed);	
		
		seeds.add(newSeed);
		SoundManager.playSound(Constants.SOUND_SEED_SHOT, 0.7f);
	}
	
	protected void createLaser( Ship sourceShip ){
		int playerIndex = sourceShip.getPlayerIndex();
		Vector2 position = sourceShip.getFrontOfShip();
		Vector2 direction = sourceShip.getDirection();
		float offset = Constants.LASER_WIDTH * 1.05f;
		position.add( direction.scl(offset, offset));
		
		Laser newLaser = new Laser( WorldUtils.createLaser(world, position, direction, playerIndex) );
		addActor(newLaser);	
		
		lasers.add(newLaser);
		SoundManager.playSound(Constants.SOUND_LASER, 0.03f);
	}
	
	protected void update(Body body) {
		if (!BodyUtils.bodyInBounds(body)) {
			
			UserDataType type = BodyUtils.bodyType(body);
			switch( type ){
			case SEED:
				Seed seed = findSeed(body);
				seeds.removeValue(seed, true);
				seed.remove();
				world.destroyBody(body);
				break;
				
			case ASTEROID:
				removeAsteroidByBody(body, false);
				break;
				
			case LASER:				
				removeLaserByBody(body);
				break;
				
			default:
				world.destroyBody(body);
				break;
			}
            
			// Destroy bodies outside of the playing field?
            
        }
	}
	
	protected void removeLaserByBody( Body laserBody )	{
		Laser laser = findLaser(laserBody);
		if( laser != null ){
			lasers.removeValue(laser, true);
			laser.remove();
			world.destroyBody(laser.getBody());
		}
	}
	
	protected void removeAsteroidByBody( Body asteroidBody, boolean wasDestroyed ) {
		Asteroid asteroid = findAsteroid(asteroidBody);
		if( asteroid == null ){
			return;
		}
		
		if( wasDestroyed ){
			AsteroidDebris debris = new AsteroidDebris(asteroid);
			asteroidDebris.add(debris);
			addActor(debris);
		}
		
		asteroids.removeValue(asteroid, true);
		asteroid.remove();
		world.destroyBody(asteroidBody);
				
	}
	
	protected Asteroid findAsteroid( Body aBody )
	{
		if( aBody == null ){
			return null;
		}
		for( Asteroid asteroid : asteroids )
		{
			if( asteroid.getBody() == aBody )
			{
				return asteroid;
			}
		}
		return null;
	}
	
	protected Seed findSeed( Body aBody )
	{
		if( aBody == null ){
			return null;
		}
		for( Seed seed : seeds )
		{
			if( seed.getBody() == aBody )
			{
				return seed;
			}
		}
		return null;
	}
	
	protected Laser findLaser( Body aBody )
	{
		if( aBody == null ){
			return null;
		}
		for( Laser laser : lasers )
		{
			if( laser.getBody() == aBody )
			{
				return laser;
			}
		}
		return null;
	}
	
	protected Ship findPlayerShip( Body aBody ){
		if( player1.getBody() == aBody ){
			return player1;
		}
		if( player2.getBody() == aBody ){
			return player2;
		}
		return null;
	}
	
	protected abstract void asteroidSeedContact(Asteroid asteroid, Seed seed);
	
	protected abstract void asteroidLaserContact(Asteroid asteroid, Laser laser);
	
	protected abstract void playerLaserContact(Laser laser, Ship ship);
	
	protected void handleContact( Pair<Body, Body> contact ){
        
		Body a = contact.first;
		Body b = contact.second;
		
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.ASTEROID, UserDataType.SEED) )
        {   
        	Asteroid asteroid = findAsteroid( BodyUtils.getBodyOfType(a, b, UserDataType.ASTEROID) );
    		Seed seed = findSeed( BodyUtils.getBodyOfType(a, b, UserDataType.SEED) );
    		if ((asteroid != null) && (seed != null)) {
    			asteroidSeedContact(asteroid, seed);
    		}
        	return;
        }
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.ASTEROID, UserDataType.LASER) )
        {
        	Asteroid asteroid = findAsteroid( BodyUtils.getBodyOfType(a, b, UserDataType.ASTEROID) );
        	Laser laser = findLaser(BodyUtils.getBodyOfType(a, b, UserDataType.LASER));
        	if ((asteroid != null) && (laser != null)) {
        		asteroidLaserContact(asteroid, laser);
        	}
        	return;
        }
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.PLAYER, UserDataType.LASER) )
        {
        	Laser laser = findLaser(BodyUtils.getBodyOfType(a, b, UserDataType.LASER));        	
        	Ship ship = findPlayerShip(BodyUtils.getBodyOfType(a, b, UserDataType.PLAYER));
        	if ((ship != null) && (laser != null)) {
        		playerLaserContact(laser, ship);
        	}
        	return;
        }
	}
	
  
	@Override
	public void beginContact(Contact contact) {		
		
		Fixture fa = contact.getFixtureA();
		Body a = fa != null ? fa.getBody() : null;
		
		Fixture fb = contact.getFixtureB();
        Body b = fb != null ? fb.getBody() : null;
        
        if( BodyUtils.getBodyOfType(a, b, UserDataType.EDGE) == null )
        {
        	contactsToHandle.add( new Pair<Body,Body>(a,b) );
        }
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Body a = fixtureA.getBody();
        Body b = fixtureB.getBody();
        
        Boolean contact = contactMap.get( new Pair<UserDataType, UserDataType>( BodyUtils.bodyType(a), BodyUtils.bodyType(b) ) );
        return contact != null ? contact.booleanValue() : false;
	}
	
}
