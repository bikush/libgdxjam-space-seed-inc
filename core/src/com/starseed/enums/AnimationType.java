package com.starseed.enums;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.starseed.util.Constants;

public enum AnimationType {
	BLOOM1( String.format(Constants.ATLAS_FLOWER_FORMAT, 1), 
			Constants.ATLAS_FLOWER_REGION, 
			Constants.ATLAS_FLOWER_START_INDEX, 
			Constants.ATLAS_FLOWER_FRAME_COUNT,
			0.05f,
			PlayMode.NORMAL ),
	
	BLOOM2( String.format(Constants.ATLAS_FLOWER_FORMAT, 2), 
			Constants.ATLAS_FLOWER_REGION, 
			Constants.ATLAS_FLOWER_START_INDEX, 
			Constants.ATLAS_FLOWER_FRAME_COUNT,
			0.05f,
			PlayMode.NORMAL ),
	
	ALIEN( 	Constants.CHARACTERS_ATLAS_PATH, 
			Constants.RUNNER_RUNNING_REGION_NAMES, 
			0.1f, 
			PlayMode.LOOP ),
	
	EXHAUST( 	Constants.ATLAS_SHIP_EXHAUST,
				Constants.ATLAS_SHIP_EXHAUST_REGION, 
				1,
				Constants.ATLAS_SHIP_EXHAUST_COUNT,
				0.1f, 
				PlayMode.LOOP );
	
	
	String atlasName;
	String regionName = null;
	String[] regionNames = null;
	int startIndex;
	int count;
	private float frameDuration;
	private PlayMode playMode;
	
	AnimationType( String atlasName, String regionName, int start, int count, float frameDuration, PlayMode mode ){
		this.atlasName = atlasName;
		this.regionName = regionName;
		this.startIndex = start;
		this.count = count;
		this.frameDuration = frameDuration;
		this.playMode = mode;
	}
	
	AnimationType( String atlasName, String[] regionNames, float frameDuration, PlayMode mode ){
		this.atlasName = atlasName;
		this.regionNames = regionNames.clone();
		this.startIndex = 0;
		this.count = regionNames.length;
		this.frameDuration = frameDuration;
		this.playMode = mode;
	}
	

	public String getAtlasName() {
		return atlasName;
	}

	public String getRegionName() {
		return regionName;
	}

	public String[] getRegionNames() {
		return regionNames;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getCount() {
		return count;
	}

	public float getFrameDuration() {
		return frameDuration;
	}

	public PlayMode getPlayMode() {
		return playMode;
	}
		
	
	
}