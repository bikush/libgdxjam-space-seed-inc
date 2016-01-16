package com.starseed.actors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.UserData;
import com.starseed.util.Constants;

public class Laser extends GameActor {
	
	TextureRegion laserImage = null;
	float width = 25;
	float height = 3;

	public Laser(Body body) {
		super(body);

		Pixmap pixLaser = new Pixmap( (int)width, (int)height, Format.RGBA8888);
		pixLaser.setColor(0.95f, 0.15f, 0f, 0.75f);
		pixLaser.fill();
		laserImage = new TextureRegion( new Texture( pixLaser ) );
		
	}

	@Override
	public UserData getUserData() {
		return userData;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		// TODO: check if position alright
		batch.draw(
				laserImage, 
				screenRectangle.x, screenRectangle.y, 
				screenRectangle.width * 0.5f, -screenRectangle.height * 0.5f,
				width, height, 
				1.0f, 1.0f, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
	}

}
