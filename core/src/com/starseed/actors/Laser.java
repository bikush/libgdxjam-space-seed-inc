package com.starseed.actors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.ProjectileUserData;
import com.starseed.util.AtlasUtils;
import com.starseed.util.AtlasUtils.TextureGenerator;
import com.starseed.util.Constants;

public class Laser extends GameActor implements TextureGenerator {
	
	private static TextureRegion laserImage = null;
	float width = 25;
	float height = 3;

	public Laser(Body body) {
		super(body);
		laserImage = AtlasUtils.getGeneratedTexture("laser", this);
	}
	
	public TextureRegion generate() {
		Pixmap pixLaser = new Pixmap( (int)width, (int)height, Format.RGBA8888);
		pixLaser.setColor(0.95f, 0.15f, 0f, 0.75f);
		pixLaser.fill();
		return new TextureRegion( new Texture( pixLaser ) );			
	}

	@Override
	public ProjectileUserData getUserData() {
		return (ProjectileUserData) userData;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		Vector2 offset = new Vector2(1,0);
		offset.rotateRad(body.getAngle());
		offset.setLength(width * 0.5f);
		
		batch.draw(
				laserImage, 
				screenRectangle.x - offset.x, screenRectangle.y - offset.y, 
				screenRectangle.width * 0.5f, -screenRectangle.height * 0.5f + height * 0.5f,
				width, height, 
				1.0f, 1.0f, 
				body.getAngle() * Constants.RAD_TO_DEG 
			);
	}

}
