package com.starseed.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AtlasUtils {
	
	private static HashMap<String, TextureAtlas> atlases = new HashMap<String, TextureAtlas>(16);
	
	public static TextureAtlas getTextureAtlas( String atlasPath ){
		TextureAtlas atlas = atlases.get(atlasPath);
		if( atlas == null ){
			atlas = new TextureAtlas( atlasPath );
			atlases.put( atlasPath, atlas );
		}
		return atlas;
	}
	
	public static TextureRegion[] getAnimationFrames( String atlasPath, String frameNameFormat, int startingIndex, int count ){
		TextureAtlas atlas = getTextureAtlas(atlasPath);
		TextureRegion[] frames = new TextureRegion[count];
        for( int i = 0; i<count; i++ )
        {
        	frames[i] = atlas.findRegion( frameNameFormat, startingIndex + i );    
        }
		return frames;
	}
}
