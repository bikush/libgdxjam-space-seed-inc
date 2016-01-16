package com.starseed.stages;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.Background;
import com.starseed.actors.Ship;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.starseed.util.WorldUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreenStage extends Stage {
	private MainScreen mainScreen;
	private UIStyle style;
	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;
	private World world;
	private Ship player1 = null;
	private Ship player2 = null;
	private float time;
	private int actionID=0;
	private Label variableLabel=null;
	HashMap<Integer,Float> actionMap;
	public MainScreenStage(MainScreen mainScreen) {
		super(new FitViewport(
			  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		this.mainScreen = mainScreen;
		style = UIStyle.getSingleton();
		setUpMainStage();
		actionMap = new HashMap<Integer,Float>();
		defineActionMap();
		
	}
	private void defineActionMap() {
		actionMap.put(0, 0.75f);  // fire seed
		actionMap.put(1, 0.6f);  // go up
		actionMap.put(2, 0.2f);  // stop
		actionMap.put(3, 0.5f);  // go left
		actionMap.put(4, 0.5f);  // stop going left, position: pi
		actionMap.put(5, 0.75f);  // fire lasers
		actionMap.put(6, 0.6f);  // go down
		actionMap.put(7, 0.2f);  // stop
		actionMap.put(8, 0.5f);  // go right
		actionMap.put(9, 0.5f);  // stop going right, position: 0
		time = -1.0f;
	}
	public void setUpMainStage() {
		world = WorldUtils.createWorld();
		addActor(new Background());
		addRocketButtons();
		addLabel("Space seed INC.", 52, Color.WHITE,
				 100, 680, true);
		addLabel(generateSubtitleText(), 30, Color.WHITE,
				 130, 630, false);
		String credits = "Coders:\n    Bruno Mikus\n    Marija Dragojevic\nArtist:\n    Ivana Berkovic\n\nlibGDX JAM: January 2016";
		addLabel(credits, 28, Color.WHITE,
				 130, 50, false);
		String var_text = "Player 1:  W          Player 2:  Up";
		variableLabel = addLabel(var_text, 28, Color.WHITE, 580, 430, false);
		setUpShips();
	}
	
    private void setUpShips() {
        player1 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH * 0.65f, Constants.WORLD_HEIGHT * 0.09f), 0f), 1 );
        addActor(player1);
        player2 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH * 0.85f, Constants.WORLD_HEIGHT * 0.09f), 0f), 2 );
        addActor(player2);
    }
	
	private String generateSubtitleText() {
		String texts [] = {"Resistance is futile. Your solar system will be inseminated.",
						   "Bringing life to you starborhood.",
						   "Need for seed.",
						   "Every seed is needed in your starborhood.",
						   "Every seed is sacred."};
		
		return texts[new Random().nextInt(texts.length)];
	}
	
	private void addRocketButtons() {
		final TextButton startButton=new TextButton("", style.getRightRocketButtonStyle());
		startButton.setPosition(100, 390);
		this.addActor(startButton);
		startButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				mainScreen.goToNextScreen = true;
			}
		});
		final TextButton quitButton=new TextButton("", style.getLeftRocketButtonStyle());
		quitButton.setPosition(100, 288);
		this.addActor(quitButton);
		quitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
	}
	
	private Label addLabel(String text, int fontSize, Color fontColor, 
						  int posX, int posY, Boolean istitle) {
		LabelStyle lStyle = (istitle) ? style.getTitleLabelStyle(fontSize, fontColor):
									  style.getLabelStyle(fontSize, fontColor);
		final Label label = new Label(text, lStyle);
		label.setPosition(posX, posY);
		this.addActor(label);
		return label;
	}
	
	@Override
	public void dispose () {
		style.dispose();
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		// TODO Auto-generated method stub
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
			player1.getBody().setTransform(player1.getBody().getPosition(), 0f);
			player1.getBody().setAngularVelocity(0f);
			player2.getBody().setTransform(player2.getBody().getPosition(), 0f);
			player2.getBody().setAngularVelocity(0f);
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			var_text = "Player 1:  Q          Player 2:  Shift";
			variableLabel.setText(var_text);
			break;
		case 1:
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			var_text = "Player 1:  W          Player 2:  Up";
			variableLabel.setText(var_text);
			break;
		case 2:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 3:
			player1.setTurnLeft(true);
			player2.setTurnLeft(true);
			var_text = "Player 1:  A          Player 2:  Left";
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
			var_text = "Player 1:  Q          Player 2:  Shift";
			variableLabel.setText(var_text);
			break;
		case 6:
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			var_text = "Player 1:  W          Player 2:  Up";
			variableLabel.setText(var_text);
			break;
		case 7:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 8:
			player1.setTurnRight(true);
			player2.setTurnRight(true);
			var_text = "Player 1:  D          Player 2:  Right";
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
		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}
}
