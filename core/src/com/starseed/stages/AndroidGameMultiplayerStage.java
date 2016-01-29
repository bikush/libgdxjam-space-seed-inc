package com.starseed.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.starseed.actors.Ship;
import com.starseed.screens.GameMultiplayerScreen;
import com.starseed.util.Constants;

public class AndroidGameMultiplayerStage extends GameMultiplayerStage {

	public AndroidGameMultiplayerStage(GameMultiplayerScreen gameScreen) {
		super(gameScreen);
		rearrangeLabels();
		createPlayerButtons(player1, false);
		createPlayerButtons(player2, true);
		adjustInstructionWindow();
	}
	
	private void adjustInstructionWindow() {
		instructionWindow.addListener( new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if( !gameInProgress ){
					if( time >= 0 ){
						gameInProgress = true;
						hideInstructionWindow();
					}else{
						gameScreen.goBack = true;
					}
				}
			}
		});
	}

	private void rearrangeLabels(){
		player1Label.setPosition( Constants.APP_WIDTH * 0.5f - player1Label.getWidth(), 5);
		player1Points.setPosition( Constants.APP_WIDTH * 0.5f + 5, 5);
		
		Group player2Group = new Group();
		player2Group.addActor(player2Label);
		player2Label.setPosition( - player2Label.getWidth(), 0 );
		player2Group.addActor(player2Points);
		player2Points.setPosition( 5, 0 );
		player2Group.setRotation(180);
		player2Group.setPosition( Constants.APP_WIDTH * 0.5f, Constants.APP_HEIGHT - 5);
		addActor(player2Group);
		
		Group timeGroup = new Group();
		timeGroup.addActor(timeLeftLabel);
		timeLeftLabel.setPosition(0, 0);
		timeGroup.addActor(timeLabel);
		timeLabel.setPosition( timeLeftLabel.getWidth() + 5, 0);
		addActor(timeGroup);
		timeGroup.setPosition(30, Constants.APP_HEIGHT * 0.5f - timeLeftLabel.getWidth() * 0.7f);
		timeGroup.setRotation(90);
	}

	private void createPlayerButtons( final Ship player, final boolean flip ){
		
		final float height = 75;
		final float buffer = 5;
		final float shootWidth = 100;
		final float moveWidth = 75;
		final float Y = flip ? (Constants.APP_HEIGHT - height - buffer) : buffer;
		
		
		final TextButton shootLaser = new TextButton( "LASER", style.getBlankButtonStyle());
		shootLaser.setPosition( flip ? Constants.APP_WIDTH - (buffer + shootWidth) : buffer, Y);
		shootLaser.setSize(shootWidth, height);
		if( flip ){
			shootLaser.getLabel().setFontScaleY( -1.0f );
			shootLaser.getLabel().setFontScaleX( -1.0f );
		}
		this.addActor(shootLaser);
		shootLaser.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if( super.touchDown(event, x, y, pointer, button) ){
					return true;
				}				
				return false;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if( !isPressed() ){
					
				}
			}
			public void clicked (InputEvent event, float x, float y) {
				if( gameInProgress ){
					createLaser(player);
				}
			}
		});
		
		final TextButton shootSeed = new TextButton("SEED", style.getBlankButtonStyle());
		shootSeed.setPosition( flip ? Constants.APP_WIDTH - 2*(buffer + shootWidth) : shootWidth+2*buffer  , Y);
		shootSeed.setSize(shootWidth, height);
		if( flip ){
			shootSeed.getLabel().setFontScaleY( -1.0f );
			shootSeed.getLabel().setFontScaleX( -1.0f );
		}
		this.addActor(shootSeed);
		shootSeed.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if( gameInProgress ){
					createSeed(player);
				}
			}
		});
		
		final TextButton turnRight = new TextButton(">", style.getBlankButtonStyle());
		turnRight.setPosition( flip ?  buffer : Constants.APP_WIDTH - (moveWidth + buffer), Y);
		turnRight.setSize(moveWidth, height);
		if( flip ){
			turnRight.getLabel().setFontScaleY( -1.0f );
			turnRight.getLabel().setFontScaleX( -1.0f );
		}
		this.addActor(turnRight);
		turnRight.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if( super.touchDown(event, x, y, pointer, button) ){
					//if( flip ){
					//	player.setTurnLeft(true);
					//}else{
						player.setTurnRight(true);
					//}
					return true;
				}				
				return false;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if( !isPressed() ){
					//if( flip ){
					//	player.setTurnLeft(false);
					//}else{
						player.setTurnRight(false);
					//}
				}
			}
		});
		
		final TextButton engine = new TextButton("E", style.getBlankButtonStyle());
		engine.setPosition( flip ? 2*buffer + moveWidth : Constants.APP_WIDTH - 2*(moveWidth + buffer), Y);
		engine.setSize(moveWidth, height);
		if( flip ){
			engine.getLabel().setFontScaleY( -1.0f );
			engine.getLabel().setFontScaleX( -1.0f );
		}
		this.addActor(engine);
		engine.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if( super.touchDown(event, x, y, pointer, button) ){
					player.setEngineOn(true);
					return true;
				}				
				return false;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if( !isPressed() ){
					player.setEngineOn(false);
				}
			}
		});
		
		final TextButton turnLeft = new TextButton("<", style.getBlankButtonStyle());
		turnLeft.setPosition( flip ? 3*buffer + 2*moveWidth : Constants.APP_WIDTH - 3*(moveWidth + buffer), Y);
		turnLeft.setSize(moveWidth, height);
		if( flip ){
			turnLeft.getLabel().setFontScaleY( -1.0f );
			turnLeft.getLabel().setFontScaleX( -1.0f );
		}
		this.addActor(turnLeft);
		turnLeft.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if( super.touchDown(event, x, y, pointer, button) ){
					//if( flip ){
					//	player.setTurnRight(true);
					//}else{
						player.setTurnLeft(true);
					//}
					return true;
				}				
				return false;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if( !isPressed() ){
					//if( flip ){
					//	player.setTurnRight(false);
					//}else{
						player.setTurnLeft(false);
					//}
				}
			}
		});
	}
	
}
