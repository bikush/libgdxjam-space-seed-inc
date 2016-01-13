package com.starseed.screens;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {
	public Boolean goBack;
	public Boolean goToNextScreen;
	private AbstractScreen backScreen;
	private AbstractScreen nextScreen;
	
	public AbstractScreen() {
		// TODO Auto-generated constructor stub
		goBack = false;
		goToNextScreen = false;
	}

	public AbstractScreen getBackScreen() {
		return backScreen;
	}

	public void setBackScreen(AbstractScreen backScreen) {
		this.backScreen = backScreen;
	}

	public AbstractScreen getNextScreen() {
		return nextScreen;
	}

	public void setNextScreen(AbstractScreen nextScreen) {
		this.nextScreen = nextScreen;
	}
	
}
