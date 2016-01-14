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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreenStage extends Stage {
	private MainScreen mainScreen;
	private Skin skin;
	
	public MainScreenStage(MainScreen mainScreen) {
		super(new FitViewport(
			  Constants.APP_WIDTH, Constants.APP_HEIGHT ));
		this.mainScreen = mainScreen;
		setUpMainStage();
	}
	
	public void setUpMainStage() {
		addActor(new Background());
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.GAME_FONT));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?: ";
		// These characters should not repeat! 

		BitmapFont bfont24 = generator.generateFont(parameter);
		
		parameter.size = 16;
		BitmapFont bfont16 = generator.generateFont(parameter);
		
		
		generator.dispose();
		
		skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal(Constants.BUTTON_ATLAS_PATH)));
		skin.add("default-24", bfont24);
		skin.add("default", bfont16);

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("button_off");
		textButtonStyle.down = skin.newDrawable("button_off");
		textButtonStyle.over = skin.newDrawable("button_on");
		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);

		// Create start game button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final TextButton startButton=new TextButton("PLAY",textButtonStyle);
		startButton.setPosition(50, 390);
		this.addActor(startButton);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		startButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				mainScreen.goToNextScreen = true;
			}
		});

		// Create quit game button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		
		final TextButton quitButton=new TextButton("QUIT", textButtonStyle);
		quitButton.setPosition(50, 288);
		this.addActor(quitButton);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		quitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		LabelStyle lStyle = new LabelStyle(bfont24, Color.WHITE);
		skin.add("default", lStyle);
		final Label label = new Label("Space sperm!", skin);
		label.setPosition(100, 700);
		this.addActor(label);
	}

	@Override
	public void dispose () {
		skin.dispose();
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
