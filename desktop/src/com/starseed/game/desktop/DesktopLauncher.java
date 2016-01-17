package com.starseed.game.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.starseed.game.StarSeedGame;
import com.starseed.util.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)(screenDimension.height * 0.9f);
		int width = (int)( Constants.APP_WIDTH * height / (float)Constants.APP_HEIGHT );
		if( width > screenDimension.width * 0.95f ){
			float factor = screenDimension.width * 0.95f / width;
			width = Math.round(factor * width);
			height = Math.round(factor * height);
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = width;
        config.height = height;
        new LwjglApplication(new StarSeedGame(), config);
        
	}
}
