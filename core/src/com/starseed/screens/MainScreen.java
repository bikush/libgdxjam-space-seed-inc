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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
