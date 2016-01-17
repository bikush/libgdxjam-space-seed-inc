package com.starseed.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.starseed.stages.MainScreenStage;
import com.starseed.screens.AbstractScreen;

public class MainScreen extends AbstractScreen {

	private MainScreenStage stage;
	
	public MainScreen() {
		super();
		stage = new MainScreenStage(this);
	}

	@Override
	public void show() {
		goToNextScreen = false;
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		stage.act(delta);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
