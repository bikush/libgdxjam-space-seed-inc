package com.starseed.stages;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.*;
import com.starseed.enums.AsteroidType;
import com.starseed.enums.EdgeSideType;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.util.BodyUtils;
import com.starseed.util.Constants;
import com.starseed.util.WorldUtils;

public class GameMultiplayerStage extends Stage implements ContactListener {
	
	private Array<Edge> edges = new Array<Edge>(EdgeSideType.values().length);
	private Ship player1 = null;
	private Ship player2 = null;
	private Array<Asteroid> asteroids = new Array<Asteroid>();
		
	private World world;
	private Runner runner;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	    
    private GameMultiplayerScreen gameScreen;
    
	public GameMultiplayerStage(GameMultiplayerScreen gameScreen) {
		super(new FitViewport(
				Constants.APP_WIDTH, Constants.APP_HEIGHT ));
				//, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
		this.gameScreen = gameScreen;
        setUpWorld();
        setupCamera();
        renderer = new Box2DDebugRenderer();
    }

    private void setUpWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
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

	@Override
	public void act(float delta) {
		super.act(delta);
		
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
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
		
//		Body a = contact.getFixtureA().getBody();
//        Body b = contact.getFixtureB().getBody();

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
			
		case Input.Keys.UP:
			player2.setEngineOn(false);
			break;
			
		case Input.Keys.LEFT:
			player2.setTurnLeft(false);
			break;
			
		case Input.Keys.RIGHT:
			player2.setTurnRight(false);
			break;
			
		default:
			break;
		
		}
		return retVal;
	}
}