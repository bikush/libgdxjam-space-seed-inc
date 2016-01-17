package com.starseed.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.screens.MainScreen;
import com.starseed.util.Constants;
import com.starseed.screens.AbstractScreen;

public class StarSeedGame extends Game {
	private Music bgMusic;
	
	@Override
	public void create () {	
		
	    setUpSound();
		GameMultiplayerScreen gameScreen = new GameMultiplayerScreen();
		MainScreen mainScreen = new MainScreen();
		mainScreen.setNextScreen(gameScreen);
		gameScreen.setBackScreen(mainScreen);
		setScreen(mainScreen);
		
	}
	
	private void setUpSound() {
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.MAIN_SOUND_FILE));
		bgMusic.play();      // plays the sound a second time, this is treated as a different instance
		bgMusic.setVolume(0.1f); 
		bgMusic.setLooping(true); // keeps the sound looping
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
