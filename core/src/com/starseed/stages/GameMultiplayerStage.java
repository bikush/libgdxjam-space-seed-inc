package com.starseed.stages;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
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

public class GameMultiplayerStage extends GameStage {
	
	private Array<Edge> edges = new Array<Edge>(EdgeSideType.values().length);
	private Array<Runner> runners= new Array<Runner>(Constants.NUMBER_OF_RUNNERS);

	private OrthographicCamera camera;
	//private Box2DDebugRenderer renderer;
	    
    private GameMultiplayerScreen gameScreen;
    
    private float time = Constants.GAME_DURATION;
    private Label timeLabel;
    
    private float nextAsteroidIn = 0.0f;
    
	private Ship player1 = null;
	private Ship player2 = null;
	
    private Label player1Points;
    private int p1Points = 0;
    private int p2Points = 0;
    private Label player2Points;
    private Boolean gameInProgress=false;
    private Image instructionWindow = null;
    private Label instruction = null;
    
	public GameMultiplayerStage(GameMultiplayerScreen gameScreen) {
		super();
				//, new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
		this.gameScreen = gameScreen;
        setUpWorld();
        setupCamera();
        setupUI();
        
        SoundManager.stopEngine(1);
        SoundManager.stopEngine(2);
        //renderer = new Box2DDebugRenderer();  
    }
	
	private void setupUI() {
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
		
		String rules = "Welcome Space Seed INC. pilots!\n\n";
		rules += "Gain points by planting seeds on asteroids.\n";
		rules += "Reduce opponent's points by shooting lasers\n";
		rules += "at asteroids they inseminated.\n"; 
		rules += "You loose points by shooting at your opponent.\n\n";
		rules += "Mr. Orange:   W (engines), A + D (turning),\n                                ";
		rules += "Shift (seeds), Ctrl (lasers).\n\n";
		rules += "Ms. Purple:    UP (engines), RIGHT + LEFT (turning),\n                               ";
	    rules += String.format("Shift (seeds), %s (lasers).\n\n", isMac ? "Alt": "Ctrl");
		rules += "May the best inseminator win!\n\n";
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
        
        ships.add(player1);
        ships.add(player2);
        
    }    

    private void setUpAsteroids() {
    	asteroids.clear();
	}

	private void setupCamera() 	{
		camera = new OrthographicCamera(Constants.APP_WIDTH/Constants.WORLD_TO_SCREEN, Constants.APP_HEIGHT/Constants.WORLD_TO_SCREEN);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
		camera.update();
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
	
	private float hitSounds = 0;
		
	public void prePhysics(float delta) {
		if (gameInProgress) {
			time -= delta;
			if (time >= 0f) {
				timeLabel.setText(String.format("%02d : %02d", (int)time/60, (int)time%60));
			} else {
				gameInProgress = false;
				player1.setEngineOn(false);
				player2.setEngineOn(false);
				showEndingWindow();
			}
			
			nextAsteroidIn -= delta;
			if( nextAsteroidIn <= 0.0f ){
				if( Constants.GAME_DURATION - time < 10f ){
					nextAsteroidIn += Constants.ASTEROID_SPAWN_DELAY_MIN;
				}else{				
					nextAsteroidIn += RandomUtils.rangeFloat( Constants.ASTEROID_SPAWN_DELAY_MIN, Constants.ASTEROID_SPAWN_DELAY_MAX);
				}
				
				Vector2 position = new Vector2();
				Vector2 velocity = new Vector2();
				if( time > Constants.GAME_DURATION * 0.5f ){
					position = new Vector2( 
							Constants.WORLD_WIDTH + Constants.WORLD_HALF_WIDTH * 0.25f,
							Constants.WORLD_HEIGHT * RandomUtils.rangeFloat( 0.1f, 0.9f)
							);
					velocity = new Vector2(
							RandomUtils.rangeFloat(-15f, -2.5f),
							RandomUtils.rangeFloat(-3f, 3f)
							);
				}else{
					position = new Vector2( 
							-Constants.WORLD_HALF_WIDTH * 0.25f,
							Constants.WORLD_HEIGHT * RandomUtils.rangeFloat( 0.1f, 0.9f)
							);
					velocity = new Vector2(
							RandomUtils.rangeFloat(15f, 2.5f),
							RandomUtils.rangeFloat(-3f, 3f)
							);
				}
				Asteroid newAsteroid =  new Asteroid( WorldUtils.createAsteroid( world, AsteroidType.getRandomType(), position) );
				newAsteroid.getBody().setLinearVelocity( velocity );
				asteroids.add( newAsteroid );
				addActor( newAsteroid );
			}
		}
	}
	
	@Override
	protected void removeAsteroidByBody( Body asteroidBody, boolean wasDestroyed ) {
		Vector2 velocity = new Vector2( asteroidBody.getLinearVelocity() );
		Asteroid asteroid = findAsteroid(asteroidBody);
		super.removeAsteroidByBody(asteroidBody, wasDestroyed);
		
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
	public void beginContact(Contact contact) {		
		
		Fixture fa = contact.getFixtureA();
		Body a = fa != null ? fa.getBody() : null;
		
		Fixture fb = contact.getFixtureB();
        Body b = fb != null ? fb.getBody() : null;
        
        if( BodyUtils.getBodyOfType(a, b, UserDataType.EDGE) == null )
        {
        	contactsToHandle.add( new Pair<Body,Body>(a,b) );
        	if( BodyUtils.getBodyOfType(a, b, UserDataType.PLAYER) != null )
            {
        		if( hitSounds < 6.0f ){
                 	hitSounds += 1.0f;
             		SoundManager.playSound( RandomUtils.randomElement(Constants.SOUND_SHIP_HIT));
             	}
            }
        }
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		super.keyDown(keyCode);
		if (gameInProgress) {
			for (Ship ship: ships) {
				if (ship.moveKeyHandler(keyCode, true)) {
					return true;
				}
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
			
		default:
			if (gameInProgress) {
				for (Ship ship: ships) {
					if (!ship.moveKeyHandler(keyCode, false)) {
						if (ship.shipType.getFireLaser() == keyCode) {
							createLaser(ship);
						} else if (ship.shipType.getFireSeed() == keyCode) {
							createSeed(ship);
						}
					}
				}
			}
			break;
		}
		return retVal;
	}

	@Override
	protected void asteroidSeedContact(Asteroid asteroid, Seed seed) {
		int playerSeedIndex = seed.getPlayerIndex();
    	if( asteroid.ensemenate( playerSeedIndex ) ){
    		int deltaPlayer1 = playerSeedIndex == 1 ? asteroid.getAsteroidType().getPoints() : 0;
    		int deltaPlayer2 = playerSeedIndex == 2 ? asteroid.getAsteroidType().getPoints() : 0;
    		updatePlayerPoints(deltaPlayer1, deltaPlayer2);
    	}
    	
    	world.destroyBody(seed.getBody());
    	seed.remove();
    	seeds.removeValue( seed, true );
	}

	@Override
	protected void asteroidLaserContact(Asteroid asteroid, Laser laser) {
    	
    	removeLaserByBody(laser.getBody());
    	asteroid.takeDamage();
    	
    	if( asteroid.isDestroyed() ){
    		int asteroidPlayerIndex = asteroid.getOwningPlayer();
    		int deltaPlayer1 = asteroidPlayerIndex == 1 ? -asteroid.getAsteroidType().getPoints() : 0;
    		int deltaPlayer2 = asteroidPlayerIndex == 2 ? -asteroid.getAsteroidType().getPoints() : 0;
    		updatePlayerPoints(deltaPlayer1, deltaPlayer2);
    		
    		removeAsteroidByBody(asteroid.getBody(), true);    
    		
    		SoundManager.playSound(Constants.SOUND_ASTEROID_DESTROY);
    	}
	}

	@Override
	protected void playerLaserContact(Laser laser, Ship ship) {
		
    	int targetedPlayer = ship.getPlayerIndex();
    	if( laser.getUserData().getPlayerIndex() != targetedPlayer ){
    		int deltaPoints1 = targetedPlayer == 2 ? -250 : 0;
    		int deltaPoints2 = targetedPlayer == 1 ? -250 : 0;
    		updatePlayerPoints(deltaPoints1, deltaPoints2);
    	}
    	removeLaserByBody(laser.getBody());
	}

	@Override
	public void postPhysics(float delta) {
		hitSounds = MathUtils.clamp(hitSounds-delta, 0f, 10f);
	}
}