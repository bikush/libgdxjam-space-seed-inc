package com.starseed.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
		skin = new Skin();
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(180, 90, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont=new BitmapFont();
		skin.add("default",bfont);

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
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
		final TextButton quitButton=new TextButton("QUIT",textButtonStyle);
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
