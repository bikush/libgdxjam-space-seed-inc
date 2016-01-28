package com.starseed.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.starseed.enums.AnimationType;

public class AtlasUtils {
	
	private static HashMap<String, TextureAtlas> atlases = new HashMap<String, TextureAtlas>(16);
	private static HashMap<AnimationType, Animation> animations = new HashMap<AnimationType, Animation>(4);
	private static HashMap<String, TextureRegion> generatedTextures = new HashMap<String, TextureRegion>(16);
	
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
	
	public static TextureRegion[] getAnimationFrames( String atlasPath, String[] frameNames ){
		TextureAtlas atlas = getTextureAtlas(atlasPath);
		TextureRegion[] frames = new TextureRegion[ frameNames.length ];
        for( int i = 0; i<frameNames.length; i++ )
        {
        	frames[i] = atlas.findRegion( frameNames[i] );    
        }
		return frames;
	}
	
	public static TextureRegion[] getAnimationFrames( AnimationType data ){
		if( data.getRegionName() != null ){
			return getAnimationFrames(data.getAtlasName(), data.getRegionName(), data.getStartIndex(), data.getCount());
		}else{
			return getAnimationFrames(data.getAtlasName(), data.getRegionNames());
		}
	}
	
	public static Animation getAnimation( AnimationType type ){
		Animation animation = animations.get(type);
		if( animation == null ){
			TextureRegion[] frames = getAnimationFrames( type );
			animation = new Animation( type.getFrameDuration(), frames );
			animation.setPlayMode( type.getPlayMode() );
			animations.put(type, animation);
		}
		return animation;
	}
	
	public static TextureRegion getGeneratedTexture( String name, TextureGenerator generator ){
		TextureRegion region = generatedTextures.get(name);
		if( region == null ){
			region = generator.generate();
			generatedTextures.put(name, region);
		}
		return region;
	}
	
	public interface TextureGenerator{
		public TextureRegion generate();
	}
		
	public static void cleanup() {
		atlases.clear();
		animations.clear();
		generatedTextures.clear();
	}
}
