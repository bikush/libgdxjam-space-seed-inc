package com.starseed.stages;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.*;
import com.starseed.enums.AsteroidSizeType;
import com.starseed.enums.AsteroidType;
import com.starseed.enums.EdgeSideType;
import com.starseed.enums.UserDataType;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.util.BodyUtils;
import com.starseed.util.Constants;
import com.starseed.util.Pair;
import com.starseed.util.RandomUtils;
import com.starseed.util.SoundManager;
import com.starseed.util.WorldUtils;

public class GameMultiplayerStage extends Stage implements ContactListener, ContactFilter {
	
	private Array<Edge> edges = new Array<Edge>(EdgeSideType.values().length);
	private Ship player1 = null;
	private Ship player2 = null;
	private Array<Asteroid> asteroids = new Array<Asteroid>();
	private Array<Asteroid> oldAsteroids = new Array<Asteroid>();
	private Array<Runner> runners= new Array<Runner>(Constants.NUMBER_OF_RUNNERS);
	private Array<Seed> seeds = new Array<Seed>();
	private Array<Laser> lasers = new Array<Laser>(); 

	private Array< Pair<Body,Body> > contactsToHandle = new Array< Pair<Body,Body> >();
	
	private Array<AsteroidDebris> asteroidDebris = new Array<AsteroidDebris>();
	
	private HashMap<Pair<UserDataType, UserDataType>, Boolean> contactMap = new HashMap<Pair<UserDataType,UserDataType>, Boolean>();
		
	private World world;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	//private Box2DDebugRenderer renderer;
	    
    private GameMultiplayerScreen gameScreen;
    
    private float time = 120f;
    private Label timeLabel;
    
    private float nextAsteroidIn = 0.0f;
    
    private Label player1Points;
    private int p1Points = 0;
    private int p2Points = 0;
    private Label player2Points;
    private Boolean gameInProgress=false;
    private Image instructionWindow = null;
    private Label instruction = null;
    private UIStyle style;
    
	public GameMultiplayerStage(GameMultiplayerScreen gameScreen) {
		super(new FitViewport(
				Constants.APP_WIDTH, Constants.APP_HEIGHT ));
				//, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
		this.gameScreen = gameScreen;
		setupContactMap();
        setUpWorld();
        setupCamera();
        setupUI();
        //renderer = new Box2DDebugRenderer();       
        
    }
	
	private void setupUI() {
		style = UIStyle.getSingleton();
		this.addActor(style.addLabel("Mr. Orange:", 30, Color.WHITE, 55, 720, false));
		this.addActor(style.addLabel("Ms. Purple:", 30, Color.WHITE, 380, 720, false));
		this.addActor(style.addLabel("Time left", 30, Color.WHITE, 730, 720, false));
		timeLabel = style.addLabel("02 : 00", 30, Color.WHITE, 885, 720, false);
		this.addActor(timeLabel);
		player1Points = style.addLabel("0", 30, Color.WHITE, 240, 720, false);
		this.addActor(player1Points);
		player2Points = style.addLabel("0", 30, Color.WHITE, 550, 720, false);
		this.addActor(player2Points);
		setUpInstructionWindow();
	}
	
	private void setUpInstructionWindow() {
		Pixmap pixWindow = new Pixmap((int)(Constants.APP_WIDTH*0.8), (int)(Constants.APP_HEIGHT*0.7), Format.RGBA8888);
		pixWindow.setColor(0.05f, 0.05f, 0.1f, 0.8f);
		pixWindow.fill();
		instructionWindow = new Image( new Texture( pixWindow ) );
		instructionWindow.setPosition(100, 80);
		this.addActor(instructionWindow);
		String rules = "Welcome Space Seed Inc. pilots!\n\n";
		rules += "As you know, our company spreads life across the\n";
		rules += "vastness of the universe.\n\n" ;
		rules += "Shoot the seeds to give life to the asteroids\n";
		rules += "and gain points. Shoot the lasers to \n";
		rules += "break up the asteroids into smaller pieces.\n\n"; 
		rules += "Play nice, do not shoot your opponent! You will\n";
		rules += "loose points, too.\n\n";
		rules += "May the best inseminator win!\n\n\n";
		rules += "->   Press space to start the game.";
		instruction = style.addLabel(rules, 32, Color.WHITE, 130, 90, false);
		this.addActor(instruction);
	}
	
	private void hideInstructionWindow() {
		instructionWindow.setVisible(false);
		instruction.remove();
	}
	
	private void showEndingWindow() {
		String endText = (this.p1Points > this.p2Points) ? "Mr. Orange, " : "Ms. Purple, ";
		endText += "Victory!\nCongratulations!\n\nYou're the greatest\ninseminator!";
		instruction = style.addLabel(endText, 50, Color.WHITE, 130, 300, true);
		this.addActor(instruction);
		instructionWindow.setVisible(true);
		instruction.setVisible(true);
		String returnToMain = "->   Press escape to return to the main screen.";
		this.addActor(style.addLabel(returnToMain, 36, Color.WHITE, 130, 130, false));
		String imagePath = (this.p1Points > this.p2Points) ? "orange_winner.png" : "purple_winner.png";
		Image winner = new Image(new Texture(Gdx.files.internal(imagePath)));
		winner.setPosition(500, 150);
		winner.setRotation(30);
		this.addActor(winner);
	}
	
	private void setUpWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
        world.setContactFilter(this);
        setUpBackground();
        setUpEdges();
        setUpRunner();
        setUpAsteroids();
    }
	
	private void setUpBackground() {
    	addActor(new Background());
	}

	private void setUpEdges() {
		
		edges.clear();
		EdgeSideType[] edgeTypes = EdgeSideType.values();
		for( EdgeSideType type : edgeTypes )
		{
			Edge edge = new Edge( WorldUtils.createEdge(world, type) );
			addActor(edge);
			edges.add(edge);
		}
    }

    private void setUpRunner() {
    	
    	for (int i=0; i < Constants.NUMBER_OF_RUNNERS; i++) {
    		runners.add(new Runner(WorldUtils.createRunner(world)));
    		addActor(runners.get(i));
    	}
        
        player1 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH / 3, Constants.WORLD_HEIGHT * 0.25f), (float)Math.PI * 1.5f), 1 );
        addActor(player1);
        
        player2 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH / 3, Constants.WORLD_HEIGHT * 0.75f), (float)Math.PI * 1.5f), 2 );
        addActor(player2);
        
    }    

    private void setUpAsteroids() {
    	asteroids.clear();
    	
//    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
//    			world, AsteroidType.SMALL_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.2f))) );
//    	
//    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
//    			world, AsteroidType.SMALL_2, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.35f))) );
//    	
//    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
//    			world, AsteroidType.MEDIUM_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.5f))) );
//    	
//    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
//    			world, AsteroidType.LARGE_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.65f))) );
//    	
//    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
//    			world, AsteroidType.LARGE_2, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.8f))) );
//    	
//    	for( Asteroid asteroid : asteroids )
//    	{
//    		addActor(asteroid);
//    	}
	}

	private void setupCamera() 	{
		camera = new OrthographicCamera(Constants.APP_WIDTH/Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT/Constants.WORLD_TO_SCREEN);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
		camera.update();
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

    private void updatePlayerPoints(int deltaPlayer1, int deltaPlayer2) {
    	p1Points = Math.max( p1Points + deltaPlayer1, 0 );
    	p2Points = Math.max( p2Points + deltaPlayer2, 0 );
    	
    	if (deltaPlayer1 != 0) {
    		player1Points.setText(String.format("%d", p1Points));
    	}
    	
    	if (deltaPlayer2 != 0) {
    		player2Points.setText(String.format("%d", p2Points));
    	}
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
	
	private Ship findPlayerShip( Body aBody ){
		if( player1.getBody() == aBody ){
			return player1;
		}
		if( player2.getBody() == aBody ){
			return player2;
		}
		return null;
	}
		
	@Override
	public void act(float delta) {
		if (delta > Constants.DELTA_MAX) {
			delta = Constants.DELTA_MAX;
		}
		if (gameInProgress) {
			time -= delta;
			if (time >= 0f) {
				timeLabel.setText(String.format("%02d : %02d", (int)time/60, (int)time%60));
			} else {
				gameInProgress = false;
				showEndingWindow();
			}
			
			nextAsteroidIn -= delta;
			if( nextAsteroidIn <= 0.0f ){
				nextAsteroidIn += RandomUtils.rangeFloat( Constants.ASTEROID_SPAWN_DELAY_MIN, Constants.ASTEROID_SPAWN_DELAY_MAX);
				
				Vector2 position = new Vector2( 
						Constants.WORLD_WIDTH + Constants.WORLD_HALF_WIDTH * 0.5f,
						Constants.WORLD_HEIGHT * RandomUtils.rangeFloat( 0.1f, 0.9f)
						);
				Vector2 velocity = new Vector2(
						RandomUtils.rangeFloat(-20, -5),
						RandomUtils.rangeFloat(-3, 3)
						);
				Asteroid newAsteroid =  new Asteroid( WorldUtils.createAsteroid( world, AsteroidType.getRandomType(), position) );
				newAsteroid.getBody().setLinearVelocity( velocity );
				asteroids.add( newAsteroid );
				addActor( newAsteroid );
			}
		}
		super.act(delta);
		
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
		
		// TODO: this could be handled better with an observer pattern
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
		if( !wasDestroyed )
		{
			oldAsteroids.add(asteroid);
		}
		else
		{
			AsteroidDebris debris = new AsteroidDebris(asteroid);
			asteroidDebris.add(debris);
			addActor(debris);
		}
		asteroids.removeValue(asteroid, true);
		asteroid.remove();
		
		Vector2 velocity = new Vector2( asteroidBody.getLinearVelocity() );
		world.destroyBody(asteroidBody);
		
		if( wasDestroyed )
		{
			int asteroidCount = 0;
			AsteroidSizeType createType = null;
			AsteroidSizeType aSize = asteroid.getAsteroidType().getSize();
			if( aSize == AsteroidSizeType.LARGE ){
				createType = AsteroidSizeType.MEDIUM;
				asteroidCount = 2;
			}else if( aSize == AsteroidSizeType.MEDIUM ){
				createType = AsteroidSizeType.SMALL;
				asteroidCount = 3;
			}
			if( asteroidCount > 0 && createType != null )
			{
				Random rand = new Random();
				Vector2 position = asteroidBody.getPosition();
				Vector2 offset = new Vector2( aSize.getRadius() * 0.5f, 0);
				offset.rotateRad( rand.nextFloat() * (float)Math.PI );
				
				float angleDiff = 360 / asteroidCount;
				for( ; asteroidCount > 0; asteroidCount-- ){
					position.add( offset );
					offset.rotate( angleDiff );
					Asteroid newAsteroid =  new Asteroid( WorldUtils.createAsteroid( world, AsteroidType.getRandomType(createType), position) );
					newAsteroid.getBody().setLinearVelocity( velocity );
					asteroids.add( newAsteroid );
					addActor( newAsteroid );
				}
			}
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		//renderer.render(world, camera.combined);
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
        	if( asteroid.ensemenate( playerSeedIndex ) ){
        		int deltaPlayer1 = playerSeedIndex == 1 ? asteroid.getAsteroidType().getPoints() : 0;
        		int deltaPlayer2 = playerSeedIndex == 2 ? asteroid.getAsteroidType().getPoints() : 0;
        		updatePlayerPoints(deltaPlayer1, deltaPlayer2);
        	}
        	
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
        	
        	if( asteroid.isDestroyed() ){
        		int asteroidPlayerIndex = asteroid.getOwningPlayer();
        		int deltaPlayer1 = asteroidPlayerIndex == 1 ? -asteroid.getAsteroidType().getPoints() : 0;
        		int deltaPlayer2 = asteroidPlayerIndex == 2 ? -asteroid.getAsteroidType().getPoints() : 0;
        		updatePlayerPoints(deltaPlayer1, deltaPlayer2);
        		
        		removeAsteroidByBody(asteroidBody, true);    
        		
        		SoundManager.playSound(Constants.SOUND_ASTEROID_DESTROY, 0.2f);
        	}
        	
        	return;
        }
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.PLAYER, UserDataType.LASER) )
        {
        	Body laserBody = BodyUtils.getBodyOfType(a, b, UserDataType.LASER);        	
        	Body shipBody = BodyUtils.getBodyOfType(a, b, UserDataType.PLAYER);
        	
        	Laser laser = findLaser(laserBody);
        	Ship target = findPlayerShip(shipBody);
        	int targetedPlayer = target.getPlayerIndex();
        	if( laser.getUserData().getPlayerIndex() != targetedPlayer ){
        		int deltaPoints1 = targetedPlayer == 2 ? -100 : 0;
        		int deltaPoints2 = targetedPlayer == 1 ? -100 : 0;
        		updatePlayerPoints(deltaPoints1, deltaPoints2);
        	}
        	
        	removeLaserByBody(laserBody);

        	
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
	public boolean keyDown(int keyCode) {
		super.keyDown(keyCode);
		if (gameInProgress) {
			switch (keyCode) {
			case Input.Keys.W:
				player1.setEngineOn(true);
				break;
				
			case Input.Keys.A:
				player1.setTurnLeft(true);
				break;
				
			case Input.Keys.D:
				player1.setTurnRight(true);
				break;
				
			case Input.Keys.UP:
				player2.setEngineOn(true);
				break;
				
			case Input.Keys.LEFT:
				player2.setTurnLeft(true);
				break;
				
			case Input.Keys.RIGHT:
				player2.setTurnRight(true);
				break;
				
			default:
				break;
			
			}
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		super.keyUp(keyCode);
		Boolean retVal = false;
		
		switch (keyCode) {
		case Input.Keys.ESCAPE:
			gameScreen.goBack = true;
			retVal = true;
			break;
		
		case Input.Keys.SPACE:
			if (!gameInProgress) {
				gameInProgress = true;
				hideInstructionWindow();
			}
			break;
			
		case Input.Keys.W:
			if (gameInProgress) {
				player1.setEngineOn(false);
			}
			break;
			
		case Input.Keys.A:
			if (gameInProgress) {
				player1.setTurnLeft(false);
			}
			break;
			
		case Input.Keys.D:
			if (gameInProgress) {
				player1.setTurnRight(false);
			}
			break;
			
		case Input.Keys.Q:
			if (gameInProgress) {
				createSeed(player1);
			}
			break;
			
		case Input.Keys.E:
			if (gameInProgress) {
				createLaser(player1);
			}
			break;
			
		case Input.Keys.UP:
			if (gameInProgress) {
				player2.setEngineOn(false);
			}
			break;
			
		case Input.Keys.LEFT:
			if (gameInProgress) {
				player2.setTurnLeft(false);
			}
			break;
			
		case Input.Keys.RIGHT:
			if (gameInProgress) {
				player2.setTurnRight(false);
			}
			break;
			
		case Input.Keys.SHIFT_RIGHT:
			if (gameInProgress) {
				createSeed(player2);
			}
			break;
			
		case Input.Keys.CONTROL_RIGHT:
			if (gameInProgress) {
				createLaser(player2);
			}
			break;
			
		default:
			break;
		}
		return retVal;
	}
	
	/*
	 * Collision and contact data
	 */

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

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Body a = fixtureA.getBody();
        Body b = fixtureB.getBody();
        
        Boolean contact = contactMap.get( new Pair<UserDataType, UserDataType>( BodyUtils.bodyType(a), BodyUtils.bodyType(b) ) );
        return contact != null ? contact.booleanValue() : false;
	}
}