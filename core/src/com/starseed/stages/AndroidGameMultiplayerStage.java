package com.starseed.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
		
		createPlayerMushroom(player1, false);
		createPlayerMushroom(player2, true);
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
	
	private void createPlayerMushroom( final Ship player, final boolean flip ){
		
		final float width = 150f;
		
		Group superMushroom = new Group();
		this.addActor(superMushroom);
		superMushroom.setSize(width * 2, width * 2);
		if( flip ){
			superMushroom.setRotation(180);
			superMushroom.setPosition(width * 1.25f, Constants.APP_HEIGHT + width * 0.25f);
		}else{
			superMushroom.setPosition(Constants.APP_WIDTH - width * 1.25f, -width * 0.25f);
		}
		superMushroom.setTouchable( Touchable.enabled );
		
		Group mushroomGroup = new Group();
		superMushroom.addActor(mushroomGroup);
		mushroomGroup.setSize(width, width);
		mushroomGroup.setPosition( width * 0.25f, width * 0.25f );
		mushroomGroup.setTouchable( Touchable.disabled );
		
		Image mushroom = new Image( style.getBlankButtonStyle().up );
		mushroom.setSize(width, width);
		mushroom.setTouchable( Touchable.disabled );
		mushroomGroup.addActor(mushroom);
		
		final Image mushroomMiddle = new Image( style.getBlankButtonStyle().down );
		mushroomMiddle.setTouchable( Touchable.disabled );
		mushroomMiddle.setSize(width*0.25f, width*0.25f);
		final float mX = width * 0.5f - mushroomMiddle.getWidth() * 0.5f;
		final float mY = width * 0.5f - mushroomMiddle.getHeight() * 0.5f;
		mushroomMiddle.setPosition( mX, mY );
		mushroomGroup.addActor(mushroomMiddle);
		
		superMushroom.addListener(new ClickListener() {
			
			float startX;
			float startY;
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if( super.touchDown(event, x, y, pointer, button) ){	
					startX = x;
					startY = y;
					return true;
				}				
				return false;
			}
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				super.touchDragged(event, x, y, pointer);
				if( isPressed() ){
					mushroomMiddle.setPosition(
							MathUtils.clamp( mX - (startX-x), 0, width * 0.75f),
							MathUtils.clamp( mY - (startY-y), 0, width * 0.75f) );
					
					float adjustedX = mushroomMiddle.getX() / (width * 0.75f);
					float adjustedY = mushroomMiddle.getY() / (width * 0.75f);
					
					if( adjustedX < 0.3 ){
						player.setTurnLeft(true);
						player.leftFactor = (0.3f - adjustedX) / 0.3f;
					}else if( adjustedX > 0.7 ){
						player.setTurnRight(true);
						player.rightFactor = (adjustedX - 0.7f) / 0.3f;
					}else{
						player.setTurnLeft(false);
						player.leftFactor = 1.0f;
						player.setTurnRight(false);
						player.rightFactor = 1.0f;
					}
					
					if( adjustedY > 0.7 ){
						player.setEngineOn(true);
						player.engineFactor = (adjustedY - 0.7f) / 0.3f;
					}else{
						player.setEngineOn(false);
						player.engineFactor = 1.0f;
					}
					
				}
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if( !isPressed() ){
					player.setEngineOn(false);
					player.setTurnLeft(false);
					player.setTurnRight(false);
					mushroomMiddle.setPosition( mX, mY );
				}
			}
		});
		
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
		
		/*if( flip ){
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
		}*/
	}
	
}
