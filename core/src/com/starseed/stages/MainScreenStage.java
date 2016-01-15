package com.starseed.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.actors.Background;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreenStage extends Stage {
	private MainScreen mainScreen;
	private UIStyle style;
	
	public MainScreenStage(MainScreen mainScreen) {
		super(new FitViewport(
			  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		this.mainScreen = mainScreen;
		style = UIStyle.getSingleton();
		setUpMainStage();
	}
	
	public void setUpMainStage() {
		addActor(new Background());
		addRocketButtons();
		addLabel("Space seed INC.", 50, Color.WHITE,
				 100, 680, true);
		addLabel(generateSubtitleText(), 30, Color.WHITE,
				 130, 630, false);
		String credits = "Coders:\n    Bruno Mikus\n    Marija Dragojevic\nArtist:\n    Ivana Berkovic\n\nlibGDX JAM: January 2016";
		addLabel(credits, 30, Color.WHITE,
				 130, 50, false);
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
	
	private void addLabel(String text, int fontSize, Color fontColor, 
						  int posX, int posY, Boolean istitle) {
		LabelStyle lStyle = (istitle) ? style.getTitleLabelStyle(fontSize, fontColor):
									  style.getLabelStyle(fontSize, fontColor);
		final Label label = new Label(text, lStyle);
		label.setPosition(posX, posY);
		this.addActor(label);
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
}
