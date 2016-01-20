package com.starseed.util;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	static Sound engine1 = null;
	static Sound engine2 = null;
	
	enum SoundType {
		LASER (Constants.SOUND_LASER, 0.03f, 1.0f, 1.0f),
		SEED_SHOT (Constants.SOUND_SEED_SHOT, 0.7f, 1.0f, 1.0f),
		ASTEROID_BOOM (Constants.SOUND_ASTEROID_DESTROY, 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_1 (Constants.SOUND_ASTEROID_HIT[0], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_2 (Constants.SOUND_ASTEROID_HIT[1], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_3 (Constants.SOUND_ASTEROID_HIT[2], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_4 (Constants.SOUND_ASTEROID_HIT[3], 0.2f, 1.0f, 1.0f),
		COLLISION_1 (Constants.SOUND_SHIP_HIT[0], 0.3f, 1.0f, 1.0f),
		COLLISION_2 (Constants.SOUND_SHIP_HIT[1], 0.3f, 1.0f, 1.0f),
		COLLISION_3 (Constants.SOUND_SHIP_HIT[2], 0.3f, 1.0f, 1.0f),
		COLLISION_4 (Constants.SOUND_SHIP_HIT[2], 0.3f, 1.0f, 1.0f),
		BLOOMING (Constants.SOUND_BLOOMING, 0.25f, 0.75f, 1.25f),
		BUTTON (Constants.BUTTON_PRESSED, 0.5f, 1.0f, 1.0f);
		
		private String soundPath;
		private float volume;
		private float pitchMin;
		private float pitchMax;
		private SoundType(String soundPath, float volume, float pitchMin, float pitchMax) {
			this.soundPath = soundPath;
			this.volume = volume;
			this.pitchMin = pitchMin;
			this.pitchMax = pitchMax;
		}
		public String getSoundPath() {
			return soundPath;
		}
		public float getVolume() {
			return volume;
		}
		public float getPitchMin() {
			return pitchMin;
		}
		public float getPitchMax() {
			return pitchMax;
		}
	}
	
	private static HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	private static HashMap<String, SoundType> soundDescMap = new HashMap<String, SoundType>();
	
	public SoundManager() {
		for (SoundType sType: SoundType.values()) {
			soundMap.put(sType.soundPath, 
					     Gdx.audio.newSound(Gdx.files.getFileHandle(sType.soundPath, FileType.Internal)));
			soundDescMap.put(sType.soundPath, sType);
		}
	}

	public static long playSound( String soundPath ){
		if (!soundMap.containsKey(soundPath)) {
			soundMap.put(soundPath,
					     Gdx.audio.newSound(Gdx.files.getFileHandle(soundPath, FileType.Internal)));
		}
		Sound sound= soundMap.get(soundPath);
		float volume = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getVolume():0.5f;
		float pitchMax = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getVolume():1.0f; 
		float pitchMin = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getVolume():1.0f;
		long soundId = sound.play( volume, RandomUtils.rangeFloat(pitchMin, pitchMax), 0f );
		return soundId;
	}
	
	public static void playEngine( int playerIndex ){
		if( playerIndex == 1 ){
			if( engine1 == null ){
				engine1 = Gdx.audio.newSound( Gdx.files.getFileHandle(Constants.SOUND_ENGINE_1, FileType.Internal) );
			}
			engine1.loop(0.5f);
		}else if( playerIndex == 2 ){
			if( engine2 == null ){
				engine2 = Gdx.audio.newSound( Gdx.files.getFileHandle(Constants.SOUND_ENGINE_1, FileType.Internal) );
			}
			engine2.loop(0.5f);
		}
	}
	
	public static void stopEngine( int playerIndex ){
		if( playerIndex == 1 && engine1 != null ){
			engine1.stop();
		}
		if( playerIndex == 2 && engine2 != null ){
			engine2.stop();
		}
	}
		
}
