package com.starseed.game;

import com.badlogic.gdx.Game;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.screens.MainScreen;
import com.starseed.screens.AbstractScreen;

public class StarSeedGame extends Game {
	@Override
	public void create () {
		//setScreen( new GameMultiplayerScreen() );
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
        // TODO: check if back/next screen is null
        // if it is then quit!
        if (currentScreen.goBack) {
            setScreen(currentScreen.getBackScreen());
        } else if (currentScreen.goToNextScreen) {
            setScreen(currentScreen.getNextScreen());
        }
    }

}
