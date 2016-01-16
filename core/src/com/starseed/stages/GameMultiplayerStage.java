package com.starseed.stages;

import java.util.HashMap;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.*;
import com.starseed.enums.AsteroidType;
import com.starseed.enums.EdgeSideType;
import com.starseed.enums.UserDataType;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.util.BodyUtils;
import com.starseed.util.Constants;
import com.starseed.util.Pair;
import com.starseed.util.WorldUtils;
import com.sun.corba.se.impl.orbutil.closure.Constant;

public class GameMultiplayerStage extends Stage implements ContactListener, ContactFilter {
	
	private Array<Edge> edges = new Array<Edge>(EdgeSideType.values().length);
	private Ship player1 = null;
	private Ship player2 = null;
	private Array<Asteroid> asteroids = new Array<Asteroid>();
	private Array<Seed> seeds = new Array<Seed>();

	private Array< Pair<Asteroid, Seed> > seedContactList = new Array<Pair<Asteroid,Seed>>();
	
	private HashMap<Pair<UserDataType, UserDataType>, Boolean> contactMap = new HashMap<Pair<UserDataType,UserDataType>, Boolean>();
		
	private World world;
	private Runner runner;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	    
    private GameMultiplayerScreen gameScreen;
    
    private float time;
    private Label timeLabel;
    
    private Label player1Points;
    private int p1Points = 0;
    private int p2Points = 0;
    private Label player2Points;
    
	public GameMultiplayerStage(GameMultiplayerScreen gameScreen) {
		super(new FitViewport(
				Constants.APP_WIDTH, Constants.APP_HEIGHT ));
				//, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
		this.gameScreen = gameScreen;
		setupContactMap();
        setUpWorld();
        setupCamera();
        setupUI();
        renderer = new Box2DDebugRenderer();       
        
    }
	
	private void setupUI() {
		UIStyle style = UIStyle.getSingleton();
		this.addActor(style.addLabel("Player 1:", 30, Color.WHITE, 55, 720, false));
		this.addActor(style.addLabel("Player 2:", 30, Color.WHITE, 360, 720, false));
		this.addActor(style.addLabel("Time left", 30, Color.WHITE, 730, 720, false));
		timeLabel = style.addLabel("02 : 00", 30, Color.WHITE, 885, 720, false);
		time = 120f;
		this.addActor(timeLabel);
		player1Points = style.addLabel("0", 30, Color.WHITE, 205, 720, false);
		this.addActor(player1Points);
		player2Points = style.addLabel("0", 30, Color.WHITE, 510, 720, false);
		this.addActor(player2Points);
	}

    private void setupContactMap() {
		contactMap.clear();
		
		// Asteroid contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.ASTEROID), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.SEED), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.EDGE), false);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.ASTEROID, UserDataType.RUNNER), true);
		
		// Seed contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.SEED), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.EDGE), false);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.SEED, UserDataType.RUNNER), true);
		
		// Player ship contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.PLAYER), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.EDGE), true);
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.PLAYER, UserDataType.RUNNER), true);
		
		// Edge contacts
		contactMap.put(new Pair<UserDataType, UserDataType>(UserDataType.EDGE, UserDataType.RUNNER), false);
		
	}
    
    private void updatePlayerPoints(int deltaPlayer1, int deltaPlayer2) {
    	p1Points += deltaPlayer1;
    	p2Points += deltaPlayer2;
    	
    	if (deltaPlayer1 != 0) {
    		player1Points.setText(String.format("%d", p1Points));
    	}
    	
    	if (deltaPlayer2 != 0) {
    		player2Points.setText(String.format("%d", p2Points));
    	}
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
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
        
        player1 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH / 3, Constants.WORLD_HEIGHT * 0.25f), (float)Math.PI * 1.5f), 1 );
        addActor(player1);
        
        player2 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH / 3, Constants.WORLD_HEIGHT * 0.75f), (float)Math.PI * 1.5f), 2 );
        addActor(player2);
        
    }    

    private void setUpAsteroids() {
    	asteroids.clear();
    	
    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.SMALL_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.2f))) );
    	
    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.SMALL_2, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.35f))) );
    	
    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.MEDIUM_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.5f))) );
    	
    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.LARGE_1, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.65f))) );
    	
    	asteroids.add( new Asteroid( WorldUtils.createAsteroid(
    			world, AsteroidType.LARGE_2, new Vector2(Constants.WORLD_WIDTH * 0.75f, Constants.WORLD_HEIGHT * 0.8f))) );
    	
    	for( Asteroid asteroid : asteroids )
    	{
    		addActor(asteroid);
    	}
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
	
	private Asteroid findAsteroid( Body aBody )
	{
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
		for( Seed seed : seeds )
		{
			if( seed.getBody() == aBody )
			{
				return seed;
			}
		}
		return null;
	}
		
	private void addSeedAsteroidContact( Body a, Body b )
	{
		Asteroid asteroid = null;
		if( BodyUtils.bodyIsOfType(a, UserDataType.ASTEROID) )
		{
			asteroid = findAsteroid(a);
		}
		else if( BodyUtils.bodyIsOfType(b, UserDataType.ASTEROID) )
		{
			asteroid = findAsteroid(b);
		}
		
		Seed seed = null;
		if( BodyUtils.bodyIsOfType(a, UserDataType.SEED) )
		{
			seed = findSeed(a);
		}
		else if( BodyUtils.bodyIsOfType(b, UserDataType.SEED) )
		{
			seed = findSeed(b);
		}
		
		if( asteroid != null && seed != null )
		{
			seedContactList.add( new Pair<Asteroid,Seed>(asteroid,seed) );
		}		
	}

	@Override
	public void act(float delta) {
		if (delta > Constants.DELTA_MAX) {
			delta = Constants.DELTA_MAX;
		}
		time -= delta;
		if (time >= 0f) {
			timeLabel.setText(String.format("%02d : %02d", (int)time/60, (int)time%60));
		}
		super.act(delta);
		
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }
        
        if( seedContactList.size > 0 )
        {
	        for( Pair<Asteroid,Seed> contact : seedContactList )
	        {
	        	contact.first.ensemenate( contact.second.getPlayerIndex());
	        	
	        	world.destroyBody(contact.second.getBody());
	        	contact.second.remove();
	        	seeds.removeValue( contact.second, true );
	        }
	        seedContactList.clear();
        }

		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}

		//TODO: Implement interpolation

	}

	private void update(Body body) {
		if (!BodyUtils.bodyInBounds(body)) {
			
			UserDataType type = BodyUtils.bodyType(body);
			switch( type ){
			case SEED:
				Seed seed = findSeed(body);
				seeds.removeValue(seed, true);
				seed.remove();
				break;
				
			case ASTEROID:
				Asteroid asteroid = findAsteroid(body);
				asteroid.remove();
				break;
				
			default:
				break;
			}
            
			// Destroy bodies outside of the playing field?
            world.destroyBody(body);
        }
	}
	
	@Override
	public void draw() {
		super.draw();
		renderer.render(world, camera.combined);
	}
	
  
	@Override
	public void beginContact(Contact contact) {
		
		Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        
        if( BodyUtils.bodiesAreOfTypes(a, b, UserDataType.ASTEROID, UserDataType.SEED) )
        {
        	addSeedAsteroidContact(a, b);
        }

//        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
//                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
//            runner.hit();
//        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
//                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
//            runner.landed();
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
			
		case Input.Keys.W:
			player1.setEngineOn(false);
			break;
			
		case Input.Keys.A:
			player1.setTurnLeft(false);
			break;
			
		case Input.Keys.D:
			player1.setTurnRight(false);
			break;
			
		case Input.Keys.Q:
			createSeed(player1);
			break;
			
		case Input.Keys.UP:
			player2.setEngineOn(false);
			break;
			
		case Input.Keys.LEFT:
			player2.setTurnLeft(false);
			break;
			
		case Input.Keys.RIGHT:
			player2.setTurnRight(false);
			break;
			
		case Input.Keys.SHIFT_RIGHT:
			createSeed(player2);
			break;
			
		default:
			break;
		
		}
		return retVal;
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Body a = fixtureA.getBody();
        Body b = fixtureB.getBody();
        
        Boolean contact = contactMap.get( new Pair<UserDataType, UserDataType>( BodyUtils.bodyType(a), BodyUtils.bodyType(b) ) );
        return contact != null ? contact.booleanValue() : false;
	}
}