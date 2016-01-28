package com.starseed.util;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static SoundManager currentInstance = null;
	
	private Sound engine1 = null;
	private Sound engine2 = null;
	
	enum SoundType {
		LASER (Constants.SOUND_LASER, 0.05f, 1.0f, 1.0f),
		SEED_SHOT (Constants.SOUND_SEED_SHOT, 0.7f, 1.0f, 1.0f),
		ASTEROID_BOOM (Constants.SOUND_ASTEROID_DESTROY, 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_1 (Constants.SOUND_ASTEROID_HIT[0], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_2 (Constants.SOUND_ASTEROID_HIT[1], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_3 (Constants.SOUND_ASTEROID_HIT[2], 0.2f, 1.0f, 1.0f),
		ASTEROID_HIT_4 (Constants.SOUND_ASTEROID_HIT[3], 0.2f, 1.0f, 1.0f),
		COLLISION_1 (Constants.SOUND_SHIP_HIT[0], 0.4f, 1.0f, 1.0f),
		COLLISION_2 (Constants.SOUND_SHIP_HIT[1], 0.4f, 1.0f, 1.0f),
		COLLISION_3 (Constants.SOUND_SHIP_HIT[2], 0.4f, 1.0f, 1.0f),
		COLLISION_4 (Constants.SOUND_SHIP_HIT[2], 0.4f, 1.0f, 1.0f),
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
	
	private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	private HashMap<String, SoundType> soundDescMap = new HashMap<String, SoundType>();
	
	public SoundManager(){
		currentInstance = this;
	}
		
	public void preloadSounds() {
		for (SoundType sType: SoundType.values()) {
			if( !soundMap.containsKey(sType.soundPath) ){
				soundMap.put(
						sType.soundPath, 
					    Gdx.audio.newSound(Gdx.files.getFileHandle(sType.soundPath, FileType.Internal)));
				soundDescMap.put(sType.soundPath, sType);
			}			
		}
	}
	
	public void cleanupSounds(){
		soundMap.clear();
		soundDescMap.clear();
	}
	
	public static long playSound( String soundPath ){
		if( currentInstance == null ){
			return 0;
		}
		return currentInstance.internalPlaySound(soundPath);
	}

	public long internalPlaySound( String soundPath ){
		if (!soundMap.containsKey(soundPath)) {
			soundMap.put(soundPath,
					     Gdx.audio.newSound(Gdx.files.getFileHandle(soundPath, FileType.Internal)));
			for( SoundType type : SoundType.values() ){
				if( type.getSoundPath() == soundPath ){
					soundDescMap.put(type.soundPath, type);
					break;
				}
			}
		}
		Sound sound= soundMap.get(soundPath);
		float volume = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getVolume():0.5f;
		float pitchMax = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getPitchMax():1.0f; 
		float pitchMin = soundDescMap.containsKey(soundPath)? soundDescMap.get(soundPath).getPitchMin():1.0f;
		long soundId = sound.play( volume, RandomUtils.rangeFloat(pitchMin, pitchMax), 0f );
		return soundId;
	}
	
	public static void playEngine( int playerIndex ){
		if( currentInstance == null ){
			return;
		}
		currentInstance.internalPlayEngine(playerIndex);
	}
	
	public void internalPlayEngine( int playerIndex ){
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
		if( currentInstance == null ){
			return;
		}
		currentInstance.internalStopEngine(playerIndex);
	}
	
	public void internalStopEngine( int playerIndex ){		
		if( playerIndex == 1 && engine1 != null ){
			engine1.stop();
		}
		if( playerIndex == 2 && engine2 != null ){
			engine2.stop();
		}
	}
		
}
