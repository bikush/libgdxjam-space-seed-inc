package com.starseed.stages;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.Asteroid;
import com.starseed.actors.AsteroidDebris;
import com.starseed.actors.Background;
import com.starseed.actors.Laser;
import com.starseed.actors.Runner;
import com.starseed.actors.Seed;
import com.starseed.actors.Ship;
import com.starseed.enums.AsteroidType;
import com.starseed.enums.UserDataType;
import com.starseed.screens.MainScreen;
import com.starseed.util.BodyUtils;
import com.starseed.util.Constants;
import com.starseed.util.Pair;
import com.starseed.util.RandomUtils;
import com.starseed.util.WorldUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreenStage extends Stage implements ContactListener, ContactFilter{
	private MainScreen mainScreen;
	private UIStyle style;
	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;
	private World world;
	private Ship player1 = null;
	private Ship player2 = null;
	private float time;
	private float ateroidTime = Constants.ASTEROID_CREATION_START;
	private int actionID = 1;
	private Label variableLabel=null;
	private Array<Seed> seeds = new Array<Seed>();
	private Vector2 shipInitPos1;
	private Vector2 shipInitPos2;
	HashMap<Integer,Float> actionMap;
	private Array<Asteroid> asteroids = new Array<Asteroid>();
	private Array<Runner> runners= new Array<Runner>(Constants.NUMBER_OF_RUNNERS);
	private Array<Laser> lasers = new Array<Laser>();
	private Array<AsteroidDebris> asteroidDebris = new Array<AsteroidDebris>();
	private Boolean seedTurn = true;
	private boolean shootSeed = false;
	private boolean shootLaser = false;
	
	private Array< Pair<Body,Body> > contactsToHandle = new Array< Pair<Body,Body> >();
	private HashMap<Pair<UserDataType, UserDataType>, Boolean> contactMap = new HashMap<Pair<UserDataType,UserDataType>, Boolean>();
	
	public MainScreenStage(MainScreen mainScreen) {
		super(new FitViewport(
			  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		this.mainScreen = mainScreen;
		style = UIStyle.getSingleton();
		setUpMainStage();
		setupContactMap();
		actionMap = new HashMap<Integer,Float>();
		defineActionMap();
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
	
	private void defineActionMap() {
		actionMap.put(0, 1f);  // fire seed or laser
		actionMap.put(1, 0.6f);  // go up
		actionMap.put(2, 0.4f);  // stop
		actionMap.put(3, 0.506f);  // go left
		actionMap.put(4, 0.5f);  // stop going left, position: pi
		actionMap.put(5, 0.1f);
		actionMap.put(6, 0.6f);  // go down
		actionMap.put(7, 0.4f);  // stop
		actionMap.put(8, 0.506f);  // go right
		actionMap.put(9, 0.5f);  // stop going right, position: 0
		time = -1.0f;
	}
	
	private void createSeed( Ship sourceShip ) {		
		int playerIndex = sourceShip.getPlayerIndex();
		Vector2 position = sourceShip.getFrontOfShip();
		Vector2 direction = sourceShip.getDirection();
		float offset = Constants.SEED_RADIUS * 1.05f;
		position.add( direction.scl(offset, offset));
		
		Seed newSeed = new Seed( WorldUtils.createSeed(world, position, direction, playerIndex) );
		addActor(newSeed);	
		
		seeds.add(newSeed);
	}
	
	private void createLaser( Ship sourceShip ){
		int playerIndex = sourceShip.getPlayerIndex();
		Vector2 position = sourceShip.getFrontOfShip();
		Vector2 direction = sourceShip.getDirection();
		float offset = Constants.LASER_WIDTH * 1.05f;
		position.add( direction.scl(offset, offset));
		
		Laser newLaser = new Laser( WorldUtils.createLaser(world, position, direction, playerIndex) );
		addActor(newLaser);	
		
		lasers.add(newLaser);
	}
	
	public void setUpMainStage() {
		world = WorldUtils.createWorld();
		world.setContactListener(this);
		world.setContactFilter(this);
		addActor(new Background());
		addRocketButtons();
		this.addActor(style.addLabel("Space seed INC.", 52, Color.WHITE, 80, 680, true));
		String introText = generateSubtitleText();
		this.addActor(style.addLabel(introText, 32, Color.WHITE, 110, 620, false));
		String credits = "Coders:\n    Bruno Mikus\n    Marija Dragojevic\nArtist:\n    Ivana Berkovic\n\nlibGDX JAM: January 2016";
		this.addActor(style.addLabel(credits, 28, Color.WHITE, 110, 50, false));
		String var_text = "Mr. Orange:  W          Ms. Purple:  Up";
		variableLabel = style.addLabel(var_text, 28, Color.WHITE, 510, 410, false);
		this.addActor(variableLabel);
		setUpShips();
		setUpRunners();
		createAsteroid();
	}
	
	private void setUpRunners() {
		for (int i=0; i < Constants.NUMBER_OF_RUNNERS; i++) {
    		runners.add(new Runner(WorldUtils.createRunner(world)));
    		addActor(runners.get(i));
    	}
	}
    private void setUpShips() {
    	shipInitPos1 = new Vector2(Constants.WORLD_WIDTH * 0.65f, Constants.WORLD_HEIGHT * 0.15f);
    	shipInitPos2 = new Vector2(Constants.WORLD_WIDTH * 0.85f, Constants.WORLD_HEIGHT * 0.15f);
        player1 = new Ship( WorldUtils.createPlayerShip(world, shipInitPos1, 0f), 1 );
        addActor(player1);
        player2 = new Ship( WorldUtils.createPlayerShip(world, shipInitPos2, 0f), 2 );
        addActor(player2);
    }
	
	private String generateSubtitleText() {
		String texts [] = {"Your solar system will be inseminated.",
						   "Bringing life to you starborhood.",
						   "Space has a need ... Need for seed.",
						   "Every seed is needed in your starborhood."};
		
		return texts[new Random().nextInt(texts.length)];
	}
	
	private void addRocketButtons() {
		final TextButton startButton=new TextButton("", style.getRightRocketButtonStyle());
		startButton.setPosition(100, 370);
		this.addActor(startButton);
		startButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				mainScreen.goToNextScreen = true;
			}
		});
		final TextButton quitButton=new TextButton("", style.getLeftRocketButtonStyle());
		quitButton.setPosition(100, 268);
		this.addActor(quitButton);
		quitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
	}
	
	@Override
	public void dispose () {
		style.dispose();
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		super.keyUp(keyCode);
		Boolean retVal = false;
		
		switch (keyCode) {
		case Input.Keys.SPACE:
			mainScreen.goToNextScreen = true;
			retVal = true;
			break;
		case Input.Keys.ESCAPE:
			Gdx.app.exit();
			retVal = true;
			break;
		default:
			break;
		
		}
		return retVal;
	}
	
	public void changeAction(int actionID) {
		String var_text;
		switch (actionID) {
		case 0:
			player1.getBody().setTransform(shipInitPos1, 0f);
			player1.getBody().setAngularVelocity(0f);
			player2.getBody().setTransform(shipInitPos2, 0f);
			player2.getBody().setAngularVelocity(0f);
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			if (seedTurn) {
				var_text = "Mr. Orange:  Q          Ms. Purple:  Shift";
				seedTurn = false;
				shootSeed = true;
				shootLaser = false;
				createSeed(player1);
				createSeed(player2);
			} else {
				var_text = "Mr. Orange:  E          Ms. Purple:  CTRL";
				seedTurn = true;
				shootSeed = false;
				shootLaser = true;
				createLaser(player1);
				createLaser(player2);
			}
			variableLabel.setText(var_text);
			break;
		case 1:
			
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			shootSeed = false;
			shootLaser = false;
			var_text = "Mr. Orange:  W          Ms. Purple:  Up";
			variableLabel.setText(var_text);
			break;
		case 2:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 3:
			player1.setTurnLeft(true);
			player2.setTurnLeft(true);
			var_text = "Mr. Orange:  A          Ms. Purple:  Left";
			variableLabel.setText(var_text);
			break;
		case 4:
			player1.setTurnLeft(false);
			player2.setTurnLeft(false);			
			break;
		case 5:
			player1.getBody().setTransform(player1.getBody().getPosition(), (float)Math.PI);
			player1.getBody().setAngularVelocity(0f);
			player2.getBody().setTransform(player2.getBody().getPosition(), (float)Math.PI);
			player2.getBody().setAngularVelocity(0f);
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 6:
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			var_text = "Mr. Orange:  W          Ms. Purple:  Up";
			variableLabel.setText(var_text);
			break;
		case 7:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 8:
			player1.setTurnRight(true);
			player2.setTurnRight(true);
			var_text = "Mr. Orange:  D          Ms. Purple:  Right";
			variableLabel.setText(var_text);
			break;
		case 9:
			player1.setTurnRight(false);
			player2.setTurnRight(false);
			break;
		default:
			break;
		}
	}
	
	public void shoot() {
		if (((int)(time*100f)) % 10 == 0) {
			if (shootLaser) {
				createLaser(player1);
				createLaser(player2);
			}
		}
		if (((int)(time*100f)) % 15 == 0) {
			if (shootSeed) {
				createSeed(player1);
				createSeed(player2);
			}
		}
	}
	
    private void createAsteroid() {
    	float pos_y = Constants.WORLD_HEIGHT * 0.77f;
    	pos_y += Constants.WORLD_HEIGHT * RandomUtils.rangeFloat(-0.075f, 0.075f);
    	Asteroid asteroid = new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.getRandomType(), new Vector2(Constants.WORLD_WIDTH, pos_y)));
    	asteroid.getBody().setLinearVelocity(-15f, 0f);
    	asteroid.setHealth((int)(new Random().nextFloat()*asteroid.getAsteroidType().getHealth()));
    	asteroids.add(asteroid);
    	addActor(asteroid);
    	
	}
	
	@Override
	public void act(float delta) {
		if (delta > Constants.DELTA_MAX) {
			delta = Constants.DELTA_MAX;
		}
		super.act(delta);
		
		time -= delta;
		if (time < 0f) {
			time = actionMap.get(actionID);
			changeAction(actionID);
			actionID++;
			if (actionID > 9) {
				actionID = 0;
			}
		}
		
		ateroidTime -= delta;
		if (ateroidTime < 0f) {
			ateroidTime = RandomUtils.rangeFloat(Constants.ASTEROID_CREATION_START, Constants.ASTEROID_CREATION_END);
			createAsteroid();
		}
		shoot();
		
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }
        
        if( contactsToHandle.size > 0 ){
        	for(  Pair<Body,Body> contact : contactsToHandle ){
        		handleContact(contact);
        	}
        	contactsToHandle.clear();
        }
        
		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
		
		if( asteroidDebris.size > 0 ){
			Array<AsteroidDebris> toRemove = new Array<AsteroidDebris>();
			for( AsteroidDebris aDebris : asteroidDebris ){
				if( aDebris.isFinished() ){
					toRemove.add(aDebris);
					aDebris.remove();					
				}
			}
			asteroidDebris.removeAll(toRemove, true);
		}
	}
	
	private void update(Body body) {
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
	
	private void removeLaserByBody( Body laserBody )	{
		Laser laser = findLaser(laserBody);
		if( laser != null ){
			lasers.removeValue(laser, true);
			laser.remove();
			world.destroyBody(laser.getBody());
		}
	}
	
	private void removeAsteroidByBody( Body asteroidBody, boolean wasDestroyed ) {
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
	
	private Asteroid findAsteroid( Body aBody )
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
	
	private Seed findSeed( Body aBody )
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
	
	private Laser findLaser( Body aBody )
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
	
	private void handleContact( Pair<Body, Body> contact ){
//		Fixture fa = contact.getFixtureA();
//		Body a = fa != null ? fa.getBody() : null;
//		
//		Fixture fb = contact.getFixtureB();
//        Body b = fb != null ? fb.getBody() : null;
        
		Body a = contact.first;
		Body b = contact.second;
		
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.ASTEROID, UserDataType.SEED) )
        {        	
        	Asteroid asteroid = findAsteroid( BodyUtils.getBodyOfType(a, b, UserDataType.ASTEROID) );
    		Seed seed = findSeed( BodyUtils.getBodyOfType(a, b, UserDataType.SEED) );
    		
    		int playerSeedIndex = seed.getPlayerIndex();
        	asteroid.ensemenate( playerSeedIndex );
        	
        	world.destroyBody(seed.getBody());
        	seed.remove();
        	seeds.removeValue( seed, true );
        	return;
        }
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.ASTEROID, UserDataType.LASER) )
        {
        	Body asteroidBody = BodyUtils.getBodyOfType(a, b, UserDataType.ASTEROID);
        	Body laserBody = BodyUtils.getBodyOfType(a, b, UserDataType.LASER);
        	
        	removeLaserByBody(laserBody);

        	Asteroid asteroid = findAsteroid(asteroidBody);
        	asteroid.takeDamage();
        	// TODO: damaging update image, ah
        	
        	if( asteroid.isDestroyed() ){
        		removeAsteroidByBody(asteroidBody, true);        		
        	}
        	
        	return;
        }
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.PLAYER, UserDataType.LASER) )
        {
        	Body laserBody = BodyUtils.getBodyOfType(a, b, UserDataType.LASER);        	
        	removeLaserByBody(laserBody);
        	// TODO: reduce points
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
        
//		Body a = contact.getFixtureA().getBody();
//        Body b = contact.getFixtureB().getBody();
//        if( BodyUtils.getBodyOfType(a, b, UserDataType.LASER) != null ){
//        	contact.setEnabled(false);
//        }
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
