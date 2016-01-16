package com.starseed.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.starseed.screens.AbstractScreen;

public class StarSeedGame extends Game {
	private Sound sound;
	private long soundID;
	@Override
	public void create () {	
		// TODO: resize initial window to respond well to different desktop screen sizes
		// Aspect ratio of the game should remain the same (4/3) and should
		// be scaled to some percentage of the screen size
//		int height = (int)(Gdx.graphics.getHeight() * 0.9f);
//		int width = (int)( Constants.APP_WIDTH * height / (float)Constants.APP_HEIGHT );
//		Gdx.graphics.setWindowedMode(width, height);
	    setUpSound();
		GameMultiplayerScreen gameScreen = new GameMultiplayerScreen();
		MainScreen mainScreen = new MainScreen();
		mainScreen.setNextScreen(gameScreen);
		gameScreen.setBackScreen(mainScreen);
		setScreen(mainScreen);
		
	}
	
	private void setUpSound() {
		sound = Gdx.audio.newSound(Gdx.files.internal(Constants.MAIN_SOUND_FILE));
		soundID = sound.play(1.0f);      // plays the sound a second time, this is treated as a different instance
		sound.setVolume(soundID, 0.1f); 
		sound.setLooping(soundID, true); // keeps the sound looping
	}
	
	@Override
	public void dispose() {
		super.dispose();
		sound.stop(soundID);
		sound.dispose();
	}
	@Override
    public void render() {
		super.render();
        AbstractScreen currentScreen = (AbstractScreen) getScreen();
        if (currentScreen.goBack) {
        	AbstractScreen tmp = currentScreen.getBackScreen(); 
        	if (tmp != null) {
            	setScreen(tmp);
            } else {
            	Gdx.app.exit();
            }
        } else if (currentScreen.goToNextScreen) {
        	AbstractScreen tmp = currentScreen.getNextScreen();
            if (tmp != null) {
            	setScreen(tmp);
            } else {
            	Gdx.app.exit();
            }
        }
    }

}
