package com.starseed.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.starseed.stages.AndroidGameMultiplayerStage;
import com.starseed.stages.GameMultiplayerStage;
import com.starseed.screens.AbstractScreen;

public class GameMultiplayerScreen extends AbstractScreen {
	
	private GameMultiplayerStage stage;
	
	public GameMultiplayerScreen() {
		super();
		stage = null;
	}

	@Override
	public void show() {
		goBack = false;
		if( Gdx.app.getType() == ApplicationType.Android ){
			stage = new AndroidGameMultiplayerStage(this);
		}else{
			stage = new GameMultiplayerStage(this);
		}
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
		stage.dispose();
		stage = null;
	}

	@Override
	public void dispose() {

	}

}
