package com.starseed.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.screens.MainScreen;
import com.starseed.screens.AbstractScreen;

public class StarSeedGame extends Game {
	@Override
	public void create () {
		GameMultiplayerScreen gameScreen = new GameMultiplayerScreen();
		MainScreen mainScreen = new MainScreen();
		mainScreen.setNextScreen(gameScreen);
		gameScreen.setBackScreen(mainScreen);
		setScreen(mainScreen);
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
