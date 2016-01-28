package com.starseed.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.screens.MainScreen;
import com.starseed.util.AtlasUtils;
import com.starseed.util.Constants;
import com.starseed.util.SoundManager;
import com.starseed.util.UIStyle;
import com.starseed.screens.AbstractScreen;

public class StarSeedGame extends Game {
	private Music bgMusic;
	public UIStyle uiStyle = null;
	
	public static StarSeedGame game = null;
		
	@Override
	public void create () {	
		game = this;
				
	    setUpSound();
	    uiStyle = new UIStyle();	    
	    
		GameMultiplayerScreen gameScreen = new GameMultiplayerScreen();
		MainScreen mainScreen = new MainScreen();
		mainScreen.setNextScreen(gameScreen);
		gameScreen.setBackScreen(mainScreen);
		setScreen(mainScreen);
		
	}
	
	private void setUpSound() {
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.MAIN_SOUND_FILE));
		bgMusic.play();      // plays the sound a second time, this is treated as a different instance
		bgMusic.setVolume(0.12f); 
		bgMusic.setLooping(true); // keeps the sound looping
		
	    SoundManager.preloadSounds();
	}
	
	public void pause() {
		super.pause();
		AtlasUtils.cleanup();
		bgMusic.pause();
	}
	
	public void resume() {
		game = this;
		super.resume();
		bgMusic.play();
	}
		
	@Override
	public void dispose() {
		super.dispose();
		bgMusic.stop();
		bgMusic.dispose();
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
