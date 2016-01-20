package com.starseed.stages;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.starseed.actors.Asteroid;
import com.starseed.actors.Background;
import com.starseed.actors.Laser;
import com.starseed.actors.Runner;
import com.starseed.actors.Seed;
import com.starseed.actors.Ship;
import com.starseed.enums.AsteroidType;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.starseed.util.RandomUtils;
import com.starseed.util.SoundManager;
import com.starseed.util.WorldUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreenStage extends GameStage {
	private MainScreen mainScreen;
	private float time;
	private float ateroidTime = Constants.ASTEROID_CREATION_START;
	private int actionID = 1;
	private Label variableLabel=null;
	private Vector2 shipInitPos1;
	private Vector2 shipInitPos2;
	private Ship player1 = null;
	private Ship player2 = null;
	HashMap<Integer,Float> actionMap;
	private Array<Runner> runners= new Array<Runner>(Constants.NUMBER_OF_RUNNERS);
	
	
	private Boolean seedTurn = true;
	private boolean shootSeed = false;
	private boolean shootLaser = false;
	
	
	public MainScreenStage(MainScreen mainScreen) {
		super();
		this.mainScreen = mainScreen;
		setUpMainStage();
		actionMap = new HashMap<Integer,Float>();
		defineActionMap();   
	}
	
	private void defineActionMap() {
		actionMap.put(0, 1f);  // fire seed or laser
		actionMap.put(1, 0.6f);  // go up
		actionMap.put(2, 0.4f);  // stop
		actionMap.put(3, 0.6f);  // go left
		actionMap.put(4, 0.5f);  // stop going left, position: pi
		actionMap.put(5, 0.1f);
		actionMap.put(6, 0.6f);  // go down
		actionMap.put(7, 0.4f);  // stop
		actionMap.put(8, 0.6f);  // go right
		actionMap.put(9, 0.5f);  // stop going right, position: 0
		time = -1.0f;
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
        ships.add(player1);
        ships.add(player2);
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
				SoundManager.playSound(Constants.BUTTON_PRESSED, 0.5f);
			}
		});
		final TextButton quitButton=new TextButton("", style.getLeftRocketButtonStyle());
		quitButton.setPosition(100, 268);
		this.addActor(quitButton);
		quitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				SoundManager.playSound(Constants.BUTTON_PRESSED, 0.5f);
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
				var_text = "Mr. Orange:  Shift    Ms. Purple:  Shift";
				seedTurn = false;
				shootSeed = true;
				shootLaser = false;
				super.createSeed(player1);
				super.createSeed(player2);
			} else {
				var_text = String.format( "Mr. Orange:  Ctrl    Ms. Purple:  %s", isMac ? "Alt" : "Ctrl" );
				seedTurn = true;
				shootSeed = false;
				shootLaser = true;
				super.createLaser(player1);
				super.createLaser(player2);
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
	protected void asteroidSeedContact(Asteroid asteroid, Seed seed) {
		int playerSeedIndex = seed.getPlayerIndex();
    	asteroid.ensemenate( playerSeedIndex );
    	
    	world.destroyBody(seed.getBody());
    	seed.remove();
    	seeds.removeValue( seed, true );
	}

	@Override
	protected void asteroidLaserContact(Asteroid asteroid, Laser laser) {
    	removeLaserByBody(laser.getBody());
    	asteroid.takeDamage();
    	
    	if( asteroid.isDestroyed() ){
    		removeAsteroidByBody(asteroid.getBody(), true);    
    		SoundManager.playSound(Constants.SOUND_ASTEROID_DESTROY, 0.2f);
    	}
	}

	@Override
	protected void playerLaserContact(Laser laser, Ship ship) {  	
    	removeLaserByBody(laser.getBody());
	}

	@Override
	public void prePhysics(float delta) {
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
	}

	@Override
	public void postPhysics(float delta) {
		// TODO Auto-generated method stub
		
	}
}
