package com.starseed.stages;

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
	private int actionID;
	public MainScreenStage(MainScreen mainScreen) {
		super(new FitViewport(
			  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		this.mainScreen = mainScreen;
		style = UIStyle.getSingleton();
		setUpMainStage();
		time = 0.5f;
		actionID = 0;
	}
	
	public void setUpMainStage() {
		world = WorldUtils.createWorld();
		addActor(new Background());
		addRocketButtons();
		addLabel("Space seed INC.", 50, Color.WHITE,
				 100, 680, true);
		addLabel(generateSubtitleText(), 30, Color.WHITE,
				 130, 630, false);
		String credits = "Coders:\n    Bruno Mikus\n    Marija Dragojevic\nArtist:\n    Ivana Berkovic\n\nlibGDX JAM: January 2016";
		addLabel(credits, 30, Color.WHITE,
				 130, 50, false);
		setUpShips();
	}
	
    private void setUpShips() {
        player1 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH * 0.65f, Constants.WORLD_HEIGHT * 0.15f), 0f), 1 );
        addActor(player1);
        player1.setEngineOn(true);
        player2 = new Ship( WorldUtils.createPlayerShip(world, new Vector2(Constants.WORLD_WIDTH * 0.85f, Constants.WORLD_HEIGHT * 0.15f), 0f), 2 );
        addActor(player2);
        player2.setEngineOn(true);
    }
	
	private String generateSubtitleText() {
		return "Subtitle";
	}
	
	private void addRocketButtons() {
		final TextButton startButton=new TextButton("PLAY", style.getRightRocketButtonStyle());
		startButton.setPosition(100, 390);
		this.addActor(startButton);
		startButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				mainScreen.goToNextScreen = true;
			}
		});
		final TextButton quitButton=new TextButton("QUIT", style.getLeftRocketButtonStyle());
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
		switch (actionID) {
		case 0:
			player1.getBody().setTransform(player1.getBody().getPosition(), 0f);
			player1.getBody().setAngularVelocity(0f);
			player2.getBody().setTransform(player2.getBody().getPosition(), 0f);
			player2.getBody().setAngularVelocity(0f);
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			break;
		case 1:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 2:
			player1.setTurnLeft(true);
			player2.setTurnLeft(true);
			break;
		case 3:
			player1.setTurnLeft(false);
			player2.setTurnLeft(false);			
			break;
		case 4:
			player1.getBody().setTransform(player1.getBody().getPosition(), (float)Math.PI);
			player1.getBody().setAngularVelocity(0f);
			player2.getBody().setTransform(player2.getBody().getPosition(), (float)Math.PI);
			player2.getBody().setAngularVelocity(0f);
			player1.setEngineOn(true);
			player2.setEngineOn(true);
			break;
		case 5:
			player1.setEngineOn(false);
			player2.setEngineOn(false);
			break;
		case 6:
			player1.setTurnRight(true);
			player2.setTurnRight(true);
			break;
		case 7:
			player1.setTurnRight(false);
			player2.setTurnRight(false);
			break;
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		time -= delta;
		if (time < 0.0f) {
			time = 0.5f;
			actionID++;
			if (actionID > 7) {
				actionID = 0;
			}
			changeAction(actionID);
		}
		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}
}
